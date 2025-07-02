package com.finature.routes

import com.finature.models.SimulationRequest
import com.finature.models.SimulationResponse
import com.finature.service.InvestCalcService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.*
import java.time.format.DateTimeFormatter

private val cache = mutableMapOf<String, Pair<Instant, Double>>()

private val BR_ZONE = ZoneId.of("America/Sao_Paulo")
private val DF      = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** Retorna o último dia útil anterior (apenas considera fins de semana) */
private fun ultimoDiaUtilAnterior(ref: LocalDate = LocalDate.now(BR_ZONE)): LocalDate {
    var data = ref.minusDays(1)
    while (data.dayOfWeek == DayOfWeek.SATURDAY || data.dayOfWeek == DayOfWeek.SUNDAY) {
        data = data.minusDays(1)
    }
    return data
}

suspend fun fetchSerie(codigo: Int): Double {
    val key = codigo.toString()
    val now = Instant.now()

    // cache TTL 24 h
    cache[key]?.let { (ts, v) ->
        if (Duration.between(ts, now).toHours() < 24) return v
    }

    val dia = ultimoDiaUtilAnterior()
    val dataStr = DF.format(dia)

    val url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.$codigo/" +
              "dados?formato=json&dataInicial=$dataStr&dataFinal=$dataStr"

    // 4. baixa JSON (IO dispatcher)
    val json = withContext(Dispatchers.IO) { URL(url).readText() }

    // 5. extrai campo valor (string) → Double
    val jsonElement = Json.parseToJsonElement(json)
    
    // Verificar se é um array ou objeto com erro
    val jsonArray = when {
        jsonElement is JsonArray -> jsonElement
        jsonElement is JsonObject && jsonElement.containsKey("erro") -> {
            throw IllegalStateException("Erro da API BCB: ${jsonElement["erro"]}")
        }
        else -> {
            println("Resposta inesperada da API BCB: $json")
            throw IllegalStateException("Formato de resposta inesperado da API BCB")
        }
    }

    require(jsonArray.isNotEmpty()) { "Série vazia para $dataStr" }

    val percentualStr = jsonArray[0].jsonObject["valor"]!!.jsonPrimitive.content.replace(',', '.')
    val taxaFracao = percentualStr.toDouble() / 100.0

    cache[key] = now to taxaFracao
    return taxaFracao
}

fun Route.taxaRoutes() {
    route("/api/taxas") {
        post("/simular") {
            try {
                val request = call.receive<SimulationRequest>()
                
                // Buscar taxas via API do BCB
                val codSelic = 1178 // Código da série Selic no SGS do Bacen
                val selicAnual = fetchSerie(codSelic)
                val poupancaAnual = InvestCalcService.calcularPoupancaAnual(selicAnual)
                
                // Calcular simulações usando o service
                val dadosPoupanca = InvestCalcService.simularInvestimento(request, poupancaAnual)
                val dadosSelic = InvestCalcService.simularInvestimento(request, selicAnual)
                
                call.respond(SimulationResponse(dadosPoupanca, dadosSelic))
            } catch (e: Exception) {
                println("Erro na simulação: ${e.message}")
                e.printStackTrace()
                call.respond(mapOf("error" to "Erro na simulação: ${e.message}"))
            }
        }
    }
}

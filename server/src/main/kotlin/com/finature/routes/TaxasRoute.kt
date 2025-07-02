// server/src/routes/TaxasRoutes.kt
package com.finature.routes

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.json.*
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.pow
import java.net.URL
import java.time.*
import java.time.format.DateTimeFormatter

private val cache = mutableMapOf<String, Pair<Instant, Double>>()

private val BR_ZONE = ZoneId.of("America/Sao_Paulo")
private val DF      = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** devolve Sexta se hoje for fim-de-semana (sem feriados) */
private fun ultimoDiaUtil(ref: LocalDate = LocalDate.now(BR_ZONE)): LocalDate =
    when (ref.dayOfWeek) {
        DayOfWeek.SATURDAY -> ref.minusDays(1)
        DayOfWeek.SUNDAY   -> ref.minusDays(2)
        else               -> ref
    }

suspend fun fetchSerie(codigo: Int): Double {
    val key = codigo.toString()
    val now = Instant.now()

    // 1. cache TTL 24 h
    cache[key]?.let { (ts, v) ->
        if (Duration.between(ts, now).toHours() < 24) return v
    }

    // 2. último dia útil → mesma data de início/fim
    val dia = ultimoDiaUtil()
    val dataStr = DF.format(dia)           // "01/07/2025"

    // 3. URL no formato “dataInicial & dataFinal”
    val url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.$codigo/" +
              "dados?formato=json&dataInicial=01/07/2025&dataFinal=01/07/2025"

    // 4. baixa JSON (IO dispatcher)
    val json = withContext(Dispatchers.IO) { URL(url).readText() }

    // 5. extrai campo valor (string) → Double
    val jsonArray = Json.parseToJsonElement(json).jsonArray

    print(jsonArray) // DEBUG: imprime o JSON recebido

    require(jsonArray.isNotEmpty()) { "Série vazia para $dataStr" }

    val percentualStr = jsonArray[0].jsonObject["valor"]!!.jsonPrimitive.content.replace(',', '.')      // caso venha "14,90"
    val taxaFracao = percentualStr.toDouble() / 100.0 // 14.90 → 0.149

    cache[key] = now to taxaFracao
    return taxaFracao
}

@Serializable
data class SimulationRequest(
    val aporteInicial: Double,
    val aporteMensal: Double,
    val tempoMeses: Int
)

@Serializable
data class SimulationPoint(
    val mes: Int,
    val valor: Double
)

@Serializable
data class SimulationResponse(
    val poupanca: List<SimulationPoint>,
    val selic: List<SimulationPoint>
)

private fun anualParaMensal(taxaAnual: Double): Double = 
    (1 + taxaAnual).pow(1.0 / 12.0) - 1

private fun simularInvestimento(
    request: SimulationRequest,
    taxaMensal: Double,
    limiteMeses: Int = 600
): List<SimulationPoint> {
    val historico = mutableListOf<SimulationPoint>()
    var saldo = request.aporteInicial
    val limiteEfetivo = minOf(request.tempoMeses, limiteMeses)
    
    for (mes in 0..limiteEfetivo) {
        if (mes != 0) {
            saldo = saldo * (1 + taxaMensal) + request.aporteMensal
        }
        historico.add(SimulationPoint(mes, (saldo * 100).toInt() / 100.0))
    }
    return historico
}

fun Route.taxaRoutes() {
    route("/api/taxas") {
        // get("/selic") { 
        //     try {
        //         call.respond(fetchSerie(1178))
        //     } catch (e: Exception) {
        //         call.respond(mapOf("error" to "Erro ao buscar taxa SELIC: ${e.message}"))
        //     }
        // }
        // get("/poupanca") { 
        //     try {
        //         // Cálculo da poupança: 0,5% a.m quando SELIC > 8,5%; senão, 70% da SELIC
        //         val selic = fetchSerie(11)
        //         val poupancaAnual = if (selic > 0.085) 0.5/100*12 else selic*0.70
        //         call.respond(poupancaAnual)
        //     } catch (e: Exception) {
        //         call.respond(mapOf("error" to "Erro ao calcular taxa da poupança: ${e.message}"))
        //     }
        // }
        post("/simular") {
            try {
                val request = call.receive<SimulationRequest>()
                
                val selicAnual = fetchSerie(1178)
                val poupancaAnual = if (selicAnual > 0.085) 0.5/100*12 else selicAnual*0.70
                
                val selicMensal = anualParaMensal(selicAnual)
                val poupancaMensal = anualParaMensal(poupancaAnual)
                
                val dadosPoupanca = simularInvestimento(request, poupancaMensal)
                val dadosSelic = simularInvestimento(request, selicMensal)
                
                call.respond(SimulationResponse(dadosPoupanca, dadosSelic))
            } catch (e: Exception) {
                call.respond(mapOf("error" to "Erro na simulação: ${e.message}"))
            }
        }
    }
}

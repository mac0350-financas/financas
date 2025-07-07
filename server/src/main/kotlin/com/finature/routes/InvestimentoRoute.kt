package com.finature.routes

import com.finature.models.SimulationRequest
import com.finature.models.SimulationResponse
import com.finature.models.SimulationResponseWithDetails
import com.finature.services.InvestimentoService
import io.ktor.server.plugins.BadRequestException
import kotlinx.serialization.SerializationException
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.http.*
import java.net.URL
import java.time.*
import java.time.format.DateTimeFormatter

private val cache = mutableMapOf<String, Pair<Instant, Double>>()

private val BR_ZONE = ZoneId.of("America/Sao_Paulo")
private val DF      = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** Retorna o último dia útil anterior (apenas considera fins de semana) */
private fun ultimoDiaUtilAnterior(ref: LocalDate = LocalDate.now(BR_ZONE)): LocalDate {
    val agora = LocalDateTime.now(BR_ZONE)
    val horaPublicacao = LocalTime.of(9, 1) // 9:01 AM
    
    // Antes das 9:00 AM o Bacen ainda não publicou os dados do dia
    val dataReferencia = if (agora.toLocalTime().isBefore(horaPublicacao)) {
        ref.minusDays(2)
    } else {
        ref.minusDays(1)
    }
    
    var data = dataReferencia
    while (data.dayOfWeek == DayOfWeek.SATURDAY || data.dayOfWeek == DayOfWeek.SUNDAY) {
        data = data.minusDays(1)
    }
    
    println("Horário atual: ${agora.toLocalTime()}, usando data: $data")
    return data
}

/** Retorna o próximo dia útil anterior à data fornecida */
private fun proximoDiaUtilAnterior(data: LocalDate): LocalDate {
    var proximaData = data.minusDays(1)
    while (proximaData.dayOfWeek == DayOfWeek.SATURDAY || proximaData.dayOfWeek == DayOfWeek.SUNDAY) {
        proximaData = proximaData.minusDays(1)
    }
    return proximaData
}

suspend fun fetchSerie(codigo: Int): Double {
    val key = codigo.toString()
    val now = Instant.now()

    // cache TTL 24 h
    cache[key]?.let { (ts, v) ->
        if (Duration.between(ts, now).toHours() < 24) {
            println("Cache hit para série $codigo: $v (${Duration.between(ts, now).toHours()}h)")
            return v
        } else {
            println("Cache expired para série $codigo (${Duration.between(ts, now).toHours()}h)")
        }
    }
    println("Cache MISS para série $codigo - fazendo requisição à API BCB")
    
    // Tenta até 10 dias úteis para trás para encontrar dados válidos
    var tentativas = 0
    val maxTentativas = 10
    var diaAtual = ultimoDiaUtilAnterior()
    
    while (tentativas < maxTentativas) {
        try {
            val dataStr = DF.format(diaAtual)
            println("Tentativa ${tentativas + 1} para série $codigo na data $dataStr")

            val url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.$codigo/" +
                      "dados?formato=json&dataInicial=$dataStr&dataFinal=$dataStr"

            val json = withContext(Dispatchers.IO) { URL(url).readText() }
            val jsonElement = Json.parseToJsonElement(json)
            
            val jsonArray = when {
                jsonElement is JsonArray -> jsonElement
                jsonElement is JsonObject && jsonElement.containsKey("erro") -> {
                    println("Erro da API BCB para data $dataStr: ${jsonElement["erro"]}")
                    null // Continua para próxima data
                }
                else -> {
                    println("Resposta inesperada da API BCB para data $dataStr: $json")
                    null // Continua para próxima data
                }
            }
            
            // Se conseguiu dados válidos e não está vazio
            if (jsonArray != null && jsonArray.isNotEmpty()) {
                val ultimoItem = jsonArray.last()
                val percentualStr = ultimoItem.jsonObject["valor"]!!.jsonPrimitive.content.replace(',', '.')
                val taxaFracao = percentualStr.toDouble() / 100.0

                cache[key] = now to taxaFracao
                println("Cache STORED para série $codigo: $taxaFracao (data: $dataStr)")
                return taxaFracao
            } else {
                println("Dados vazios para série $codigo na data $dataStr")
            }
            
        } catch (e: Exception) {
            println("Erro ao buscar série $codigo na data ${DF.format(diaAtual)}: ${e.message}")
        }
        
        // Vai para o dia útil anterior
        tentativas++
        diaAtual = proximoDiaUtilAnterior(diaAtual)
    }
    
    throw IllegalStateException("Não foi possível obter dados para a série $codigo após $maxTentativas tentativas")
}

fun Route.investimentoRoute(
    /** permite injeção de stub nos testes  */
    fetchSerieFn: suspend (Int) -> Double = ::fetchSerie
) {
    route("/api/investimento") {
        post("/simular") {
            try {
                val req = call.receive<SimulationRequest>()

                val selicAnual       = fetchSerieFn(1178)         // SELIC
                val trMensalPoupanca = fetchSerieFn(226)          // TR

                val poupancaAnual = InvestimentoService.calcularPoupancaAnual(
                    selicAnual, trMensalPoupanca
                )
                val dadosPoupanca  = InvestimentoService.simularInvestimento(req, poupancaAnual)
                val dadosSelic     = InvestimentoService.simularInvestimento(req, selicAnual)

                call.respond(
                    SimulationResponseWithDetails(
                        poupanca         = dadosPoupanca,
                        selic            = dadosSelic,
                        detalhesPoupanca = InvestimentoService.calcularDetalhes(req, dadosPoupanca, true),
                        detalhesSelic    = InvestimentoService.calcularDetalhes(req, dadosSelic,  false)
                    )
                )
            }
            /* ---------- validação / corpo inválido ---------- */
            catch (e: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest,
                    mapOf("error" to "Corpo JSON inválido: ${e.message}"))
            }
            catch (e: SerializationException) {   // JSON bem-formado mas campos faltando
                call.respond(HttpStatusCode.BadRequest,
                    mapOf("error" to "Campos ausentes ou tipos inválidos: ${e.message}"))
            }
            /* ---------- demais falhas (rede, cálculo…) -------- */
            catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError,
                    mapOf("error" to "Erro na simulação: ${e.message}"))
            }
        }

        get("/taxas") {
            try {
                val selicAnual       = fetchSerieFn(1178) // SELIC
                val trMensalPoupanca = fetchSerieFn(226)  // TR

                val poupancaAnual = InvestimentoService.calcularPoupancaAnual(
                    selicAnual, trMensalPoupanca
                )

                call.respond(
                    mapOf(
                        "selicAnual" to selicAnual,
                        "poupancaAnual" to poupancaAnual
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError,
                    mapOf("error" to "Erro ao obter taxas: ${e.message}"))
            }
        }
    }

}

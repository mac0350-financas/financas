package com.finature.routes

import com.finature.models.SimulationRequest
import com.finature.models.SimulationResponseWithDetails
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class InvestimentoRouteTest {

    @Test
    fun `test successful simulation`() = testApplication {
        val mockFetchSerie: suspend (Int) -> Double = { codigo ->
            when (codigo) {
                1178 -> 0.1375 // SELIC 13.75%
                226 -> 0.005   // TR 0.5% mensal
                else -> throw IllegalArgumentException("Código desconhecido: $codigo")
            }
        }

        application {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            routing {
                investimentoRoute(mockFetchSerie)
            }
        }

        val response = client.post("/api/investimento/simular") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "aporteInicial": 1000.0,
                    "aporteMensal": 100.0,
                    "tempoMeses": 12
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("poupanca"))
        assertTrue(responseBody.contains("selic"))
        assertTrue(responseBody.contains("detalhesPoupanca"))
        assertTrue(responseBody.contains("detalhesSelic"))
    }

    @Test
    fun `test invalid JSON body`() = testApplication {
        val mockFetchSerie: suspend (Int) -> Double = { 0.1 }

        application {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            routing {
                investimentoRoute(mockFetchSerie)
            }
        }

        val response = client.post("/api/investimento/simular") {
            contentType(ContentType.Application.Json)
            setBody("{ invalid json }")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("error"))
    }

    @Test
    fun `test missing required fields`() = testApplication {
        val mockFetchSerie: suspend (Int) -> Double = { 0.1 }

        application {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            routing {
                investimentoRoute(mockFetchSerie)
            }
        }

        val response = client.post("/api/investimento/simular") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "aporteInicial": 1000.0
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("error"))
    }

    @Test
    fun `test API failure handling`() = testApplication {
        val mockFetchSerie: suspend (Int) -> Double = { _ ->
            throw RuntimeException("API BCB indisponível")
        }

        application {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            routing {
                investimentoRoute(mockFetchSerie)
            }
        }

        val response = client.post("/api/investimento/simular") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "aporteInicial": 1000.0,
                    "aporteMensal": 100.0,
                    "tempoMeses": 12
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.InternalServerError, response.status)
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("error"))
        assertTrue(responseBody.contains("Erro na simulação"))
    }
}
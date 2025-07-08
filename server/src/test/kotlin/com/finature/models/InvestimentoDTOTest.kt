package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class InvestimentoDTOTest {

    @Test
    fun `deve criar InvestimentoDTO corretamente`() {
        val investimento = InvestimentoDTO(
            usuarioId = 1,
            valorInvestido = 1000.0,
            taxa = 0.05,
            dataInicial = "2024-01-01",
            dataFinal = "2024-12-31"
        )

        assertEquals(1, investimento.usuarioId)
        assertEquals(1000.0, investimento.valorInvestido)
        assertEquals(0.05, investimento.taxa)
        assertEquals("2024-01-01", investimento.dataInicial)
        assertEquals("2024-12-31", investimento.dataFinal)
    }

    @Test
    fun `deve serializar e desserializar InvestimentoDTO corretamente`() {
        val investimento = InvestimentoDTO(
            usuarioId = 1,
            valorInvestido = 1000.0,
            taxa = 0.05,
            dataInicial = "2024-01-01",
            dataFinal = "2024-12-31"
        )

        val json = Json.encodeToString(InvestimentoDTO.serializer(), investimento)
        val investimentoDesserializado = Json.decodeFromString(InvestimentoDTO.serializer(), json)

        assertEquals(investimento, investimentoDesserializado)
    }
}
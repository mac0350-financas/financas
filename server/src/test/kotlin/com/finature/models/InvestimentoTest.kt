package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class InvestimentoTest {

    @Test
    fun `deve criar Investimento corretamente`() {
        val investimento = Investmento(
            valorBruto = 10000.0,
            rentabilidadeBruta = 5.0,
            valorIR = 150.0,
            valorLiquido = 9850.0,
            rentabilidadeLiquida = 4.85,
            ganhoLiquido = 4850.0,
            totalInvestido = 5000.0
        )

        assertEquals(10000.0, investimento.valorBruto)
        assertEquals(5.0, investimento.rentabilidadeBruta)
        assertEquals(150.0, investimento.valorIR)
        assertEquals(9850.0, investimento.valorLiquido)
        assertEquals(4.85, investimento.rentabilidadeLiquida)
        assertEquals(4850.0, investimento.ganhoLiquido)
        assertEquals(5000.0, investimento.totalInvestido)
    }

    @Test
    fun `deve serializar e desserializar Investimento corretamente`() {
        val investimento = Investmento(
            valorBruto = 10000.0,
            rentabilidadeBruta = 5.0,
            valorIR = 150.0,
            valorLiquido = 9850.0,
            rentabilidadeLiquida = 4.85,
            ganhoLiquido = 4850.0,
            totalInvestido = 5000.0
        )

        val json = Json.encodeToString(Investmento.serializer(), investimento)
        val investimentoDesserializado = Json.decodeFromString(Investmento.serializer(), json)

        assertEquals(investimento, investimentoDesserializado)
    }
}
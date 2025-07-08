package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class SimulationTest {

    @Test
    fun `deve criar SimulationRequest corretamente`() {
        val request = SimulationRequest(
            aporteInicial = 1000.0,
            aporteMensal = 200.0,
            tempoMeses = 12
        )

        assertEquals(1000.0, request.aporteInicial)
        assertEquals(200.0, request.aporteMensal)
        assertEquals(12, request.tempoMeses)
    }

    @Test
    fun `deve criar SimulationResponse corretamente`() {
        val poupanca = listOf(SimulationPoint(1, 1200.0), SimulationPoint(2, 1400.0))
        val selic = listOf(SimulationPoint(1, 1210.0), SimulationPoint(2, 1420.0))

        val response = SimulationResponse(
            poupanca = poupanca,
            selic = selic
        )

        assertEquals(poupanca, response.poupanca)
        assertEquals(selic, response.selic)
    }

    @Test
    fun `deve criar SimulationResponseWithDetails corretamente`() {
        val poupancaPoints = listOf(SimulationPoint(1, 1200.0), SimulationPoint(2, 1400.0))
        val selicPoints = listOf(SimulationPoint(1, 1210.0), SimulationPoint(2, 1420.0))

        val detalhesPoupanca = Investmento(
            valorBruto = 1500.0,
            rentabilidadeBruta = 0.05,
            valorIR = 50.0,
            valorLiquido = 1450.0,
            rentabilidadeLiquida = 0.048,
            ganhoLiquido = 450.0,
            totalInvestido = 1000.0
        )

        val detalhesSelic = Investmento(
            valorBruto = 1600.0,
            rentabilidadeBruta = 0.06,
            valorIR = 60.0,
            valorLiquido = 1540.0,
            rentabilidadeLiquida = 0.055,
            ganhoLiquido = 540.0,
            totalInvestido = 1000.0
        )

        val responseWithDetails = SimulationResponseWithDetails(
            poupanca = poupancaPoints,
            selic = selicPoints,
            detalhesPoupanca = detalhesPoupanca,
            detalhesSelic = detalhesSelic
        )

        assertEquals(poupancaPoints, responseWithDetails.poupanca)
        assertEquals(selicPoints, responseWithDetails.selic)
        assertEquals(detalhesPoupanca, responseWithDetails.detalhesPoupanca)
        assertEquals(detalhesSelic, responseWithDetails.detalhesSelic)
    }

    @Test
    fun `deve serializar e desserializar SimulationRequest corretamente`() {
        val request = SimulationRequest(
            aporteInicial = 1000.0,
            aporteMensal = 200.0,
            tempoMeses = 12
        )

        val json = Json.encodeToString(SimulationRequest.serializer(), request)
        val requestDesserializado = Json.decodeFromString(SimulationRequest.serializer(), json)

        assertEquals(request, requestDesserializado)
    }

    @Test
    fun `deve serializar e desserializar SimulationResponse corretamente`() {
        val poupanca = listOf(SimulationPoint(1, 1200.0), SimulationPoint(2, 1400.0))
        val selic = listOf(SimulationPoint(1, 1210.0), SimulationPoint(2, 1420.0))

        val response = SimulationResponse(
            poupanca = poupanca,
            selic = selic
        )

        val json = Json.encodeToString(SimulationResponse.serializer(), response)
        val responseDesserializado = Json.decodeFromString(SimulationResponse.serializer(), json)

        assertEquals(response, responseDesserializado)
    }

    @Test
    fun `deve serializar e desserializar SimulationResponseWithDetails corretamente`() {
        val poupancaPoints = listOf(SimulationPoint(1, 1200.0), SimulationPoint(2, 1400.0))
        val selicPoints = listOf(SimulationPoint(1, 1210.0), SimulationPoint(2, 1420.0))

        val detalhesPoupanca = Investmento(
            valorBruto = 1500.0,
            rentabilidadeBruta = 0.05,
            valorIR = 50.0,
            valorLiquido = 1450.0,
            rentabilidadeLiquida = 0.048,
            ganhoLiquido = 450.0,
            totalInvestido = 1000.0
        )

        val detalhesSelic = Investmento(
            valorBruto = 1600.0,
            rentabilidadeBruta = 0.06,
            valorIR = 60.0,
            valorLiquido = 1540.0,
            rentabilidadeLiquida = 0.055,
            ganhoLiquido = 540.0,
            totalInvestido = 1000.0
        )

        val responseWithDetails = SimulationResponseWithDetails(
            poupanca = poupancaPoints,
            selic = selicPoints,
            detalhesPoupanca = detalhesPoupanca,
            detalhesSelic = detalhesSelic
        )

        val json = Json.encodeToString(SimulationResponseWithDetails.serializer(), responseWithDetails)
        val desserializado = Json.decodeFromString(SimulationResponseWithDetails.serializer(), json)

        assertEquals(responseWithDetails, desserializado)
    }
}

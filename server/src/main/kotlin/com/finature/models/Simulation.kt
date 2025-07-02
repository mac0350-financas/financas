package com.finature.models

import kotlinx.serialization.Serializable

@Serializable
data class SimulationRequest(
    val aporteInicial: Double,
    val aporteMensal : Double,
    val tempoMeses   : Int
)

@Serializable
data class SimulationPoint(val mes: Int, val valor: Double)

@Serializable
data class SimulationResponse(
    val poupanca: List<SimulationPoint>,
    val selic: List<SimulationPoint>
)
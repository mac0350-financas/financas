package com.finature.models

import kotlinx.serialization.Serializable

@Serializable
data class Investmento(
    val valorBruto: Double,
    val rentabilidadeBruta: Double,
    val valorIR: Double,
    val valorLiquido: Double,
    val rentabilidadeLiquida: Double,
    val ganhoLiquido: Double,
    val totalInvestido: Double
)

package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class InvestimentoDTO(
    val usuarioId: Int,
    val valorInvestido: Double,
    val taxa: Double,
    val dataInicial: String,
    val dataFinal: String,
)

package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class MetaDTO(
    val usuarioId: Int,
    val categoria: String,
    val valorLimite: Double,
    val valorAtual: Double,
    val dataInicial: String,
    val dataFinal: String,
)


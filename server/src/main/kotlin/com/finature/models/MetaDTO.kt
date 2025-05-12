package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class MetaDTO(
    val id: Int,
    val usuarioId: Int,
    val categoria: String,
    val valorLimite: Double,
    val periodo: Double,
)


package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class MetaDTO(
    val usuarioId: Int,
    val categoria: String,
    val valorLimite: Double,
    var valorAtual: Double,
    val dataInicial: String,
    val dataFinal: String, 
    val tipoId: Int,
)


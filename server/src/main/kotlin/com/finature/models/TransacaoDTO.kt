package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class TransacaoDTO(
    val id: Int,
    val data: String,
    val valor: Double,
    val tipoId: Int,
    val categoria: String,
    val descricao: String,
    val usuarioId: Int? = null
)
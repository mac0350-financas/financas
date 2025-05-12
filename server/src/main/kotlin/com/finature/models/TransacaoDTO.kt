package com.finature.models
import java.time.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class TransacaoDTO(
    val id: Int,
    val data: LocalDate,
    val valor: Double,
    val tipoId: Int,
    val categoria: String,
    val usuarioId: Int
)
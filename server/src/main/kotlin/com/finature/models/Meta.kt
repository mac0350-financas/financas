package com.finature.models

data class Meta(
    val id: Int,
    val usuarioId: Int,
    val categoria: String,
    val valorLimite: Double,
    val periodo: Double,
)


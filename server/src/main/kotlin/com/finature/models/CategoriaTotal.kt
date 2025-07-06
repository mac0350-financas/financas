package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class CategoriaTotal(
    val categoria: String,
    val total: Double
)
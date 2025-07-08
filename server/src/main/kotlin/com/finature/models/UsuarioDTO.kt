package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDTO(
    val id: Int? = null,
    val nome: String,
    val email: String,
    val senha: String
)

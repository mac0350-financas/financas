package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDTO(
    val nome: String,
    val email: String,
    val senha: String
)

package com.finature.sessions
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioSessao(
    val id: Int? = null,
    val nome: String,
    val email: String
) 
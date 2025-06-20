package com.finature.sessions
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioSessao(
    val nome: String,
    val email: String
) 
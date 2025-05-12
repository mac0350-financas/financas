package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDTO(
    var id: Int,
    var nome: String,
    var email: String,
    var senha: String,
)

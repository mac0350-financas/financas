package com.finature.models
import kotlinx.serialization.Serializable

@Serializable
data class TipoTransacaoDTO( 
    val id: Int,
    val tipo: Int,
)
package com.finature.models
import java.time.LocalDate

data class Transacao(
    val id: Int,
    val data: LocalDate,
    val valor: Double,
    val tipo: TipoTransacao,
    val categoria: String,
    val usuarioId: Int
)
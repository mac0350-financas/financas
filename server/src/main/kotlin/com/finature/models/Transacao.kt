package models

data class Transacao(
    val id: Int,
    val data: String,
    val valor: Double,
    val tipo: TipoTransacao,
    val categoria: String,
    val usuarioId: Int
)
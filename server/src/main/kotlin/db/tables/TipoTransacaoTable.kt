package com.finature.db.tables
import org.jetbrains.exposed.sql.Table

object TipoTransacaoTable : Table("tipo_transacao") {
    val tipo = integer("tipo")
}
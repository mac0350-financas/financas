package com.finature.db.tables
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.dao.id.IntIdTable


object TipoTransacaoTable : IntIdTable("tipos_transacao") {
    val tipo = integer("tipo")
}

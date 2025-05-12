package com.finature.db.tables

import org.jetbrains.exposed.sql.Table
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.sql.javatime.date

object TransacaoTable : Table("transacoes") {
    val id = integer("id").autoIncrement() 
    val data = date("data")
    val valor = double("valor")
    val tipo = reference("tipo", TipoTransacaoTable.tipo)
    val categoria = varchar("categoria", 50)
    val usuarioId = reference("usuario_id", UsuarioTable.id)

    override val primaryKey = PrimaryKey(id)
}

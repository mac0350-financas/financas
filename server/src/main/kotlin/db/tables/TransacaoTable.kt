package com.finature.db.tables

import org.jetbrains.exposed.sql.Table
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.dao.id.IntIdTable

object TransacaoTable : IntIdTable("transacoes") {
    val data = varchar("data",50)
    val valor = double("valor")
    val tipoId = reference("tipo", TipoTransacaoTable)
    val categoria = varchar("categoria", 50)
    val usuarioId = reference("usuario_id", UsuarioTable)
}

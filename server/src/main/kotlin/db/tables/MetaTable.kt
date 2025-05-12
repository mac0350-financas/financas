package com.finature.db.tables
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.dao.id.IntIdTable

object MetaTable : IntIdTable("meta") {
    val usuarioId = reference("usuario_id", UsuarioTable)
    val categoria = varchar("categoria", 255)
    val valorLimite = double("valor_limite")
    val periodo = double("periodo")
}

package com.finature.db.tables
import org.jetbrains.exposed.sql.Table

object MetaTable : Table("meta") {
    val id = integer("id").autoIncrement()
    val usuarioId = reference("usuario_id", UsuarioTable.id)
    val categoria = varchar("categoria", 255)
    val valorLimite = double("valor_limite")
    val periodo = double("periodo")

    override val primaryKey = PrimaryKey(id)

}

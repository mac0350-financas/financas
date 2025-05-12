package com.finature.db.tables
import org.jetbrains.exposed.sql.Table

object UsuarioTable : Table("usuarios") {
    val id = integer("id").autoIncrement() 
    val nome = varchar("nome", 255)
    val email = varchar("email", 255)
    val senha = varchar("senha", 255)

    override val primaryKey = PrimaryKey(id)
}
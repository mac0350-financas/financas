package com.finature.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.dao.id.IntIdTable

object UsuarioTable : IntIdTable("usuarios") {
    val nome = varchar("nome", 255)
    val email = varchar("email", 255)
    val senha = varchar("senha", 255)
}
package com.finature.repositories

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import com.finature.models.UsuarioDTO
import com.finature.db.tables.UsuarioTable

class UsuarioRepository {
    
    fun salvaUsuario(usuario: UsuarioDTO) {
        transaction {
            UsuarioTable.insert {
                it[nome] = usuario.nome
                it[email] = usuario.email
                it[senha] = usuario.senha
            }
        }
    }
    
    fun verificaEmailExistente(email: String): UsuarioDTO? {
        return transaction {
            UsuarioTable.select { UsuarioTable.email eq email }
                .map {
                    UsuarioDTO(
                        id = it[UsuarioTable.id].value,
                        nome = it[UsuarioTable.nome],
                        email = it[UsuarioTable.email],
                        senha = it[UsuarioTable.senha]
                    )
                }.singleOrNull()
        }
    }
}

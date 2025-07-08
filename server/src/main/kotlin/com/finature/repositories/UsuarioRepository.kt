package com.finature.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import com.finature.models.UsuarioDTO
import com.finature.db.dao.UsuarioDAO

class UsuarioRepository {
    
    fun salvaUsuario(usuario: UsuarioDTO) {
        transaction {
            UsuarioDAO.new {
                nome = usuario.nome
                email = usuario.email
                senha = usuario.senha
            }
        }
    } 
    
    fun verificaEmailExistente(email: String): UsuarioDTO? {
        return transaction {
            UsuarioDAO.find { com.finature.db.tables.UsuarioTable.email eq email }
                .firstOrNull()?.toDTO()
        }
    }
}
 
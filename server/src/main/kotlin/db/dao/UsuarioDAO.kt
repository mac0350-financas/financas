package com.finature.db.dao

import com.finature.db.tables.UsuarioTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.models.UsuarioDTO

class UsuarioDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UsuarioDAO>(UsuarioTable)

    var nome by UsuarioTable.nome
    var email by UsuarioTable.email
    var senha by UsuarioTable.senha

    fun toDTO() = UsuarioDTO(
        id = this.id.value,
        nome = nome,
        email = email,
        senha = senha
    )
}

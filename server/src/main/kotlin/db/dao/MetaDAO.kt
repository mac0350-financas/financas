package com.finature.db.dao

import com.finature.db.tables.MetaTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.models.MetaDTO

class MetaDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MetaDAO>(MetaTable)

    var usuarioId by MetaTable.usuarioId
    var categoria by MetaTable.categoria
    var valorLimite by MetaTable.valorLimite
    var periodo by MetaTable.periodo

    fun toDTO() = MetaDTO(
        id = this.id.value,
        usuarioId = usuarioId.value,
        categoria = categoria,
        valorLimite = valorLimite,
        periodo = periodo
    )
}

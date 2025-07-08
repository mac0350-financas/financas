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
    var valorAtual by MetaTable.valorAtual
    var dataInicial by MetaTable.dataInicial
    var dataFinal by MetaTable.dataFinal
    var tipoId by MetaTable.tipoId

    fun toDTO() = MetaDTO(
        usuarioId = usuarioId.value,
        categoria = categoria,
        valorLimite = valorLimite,
        valorAtual = valorAtual, 
        dataInicial = dataInicial,
        dataFinal = dataFinal,
        tipoId = tipoId.value
    ) 
}

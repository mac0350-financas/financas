package com.finature.db.dao

import com.finature.db.tables.TransacaoTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.models.TransacaoDTO

class TransacaoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TransacaoDAO>(TransacaoTable)

    var usuarioId by TransacaoTable.usuarioId
    var data by TransacaoTable.data
    var valor by TransacaoTable.valor
    var tipoId by TransacaoTable.tipoId
    var categoria by TransacaoTable.categoria
    var descricao by TransacaoTable.descricao

    fun toDTO() = TransacaoDTO(
        usuarioId = usuarioId.value, 
        data = data,
        valor = valor,
        tipoId = tipoId.value,
        descricao = descricao,
        categoria = categoria
    )
}

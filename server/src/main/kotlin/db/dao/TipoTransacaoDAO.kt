package com.finature.db.dao

import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.models.TipoTransacaoDTO

class TipoTransacaoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TipoTransacaoDAO>(TipoTransacaoTable)

    var tipo by TipoTransacaoTable.tipo

    fun toDTO() = TipoTransacaoDTO(
        id = this.id.value,
        tipo = tipo
    )
}

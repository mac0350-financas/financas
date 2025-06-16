package com.finature.db.dao

import com.finature.db.tables.InvestimentoTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.models.InvestimentoDTO

class InvestimentoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InvestimentoDAO>(InvestimentoTable)

    var usuarioId by InvestimentoTable.usuarioId
    var valorInvestido by InvestimentoTable.valorInvestido
    var taxa by InvestimentoTable.taxa
    var dataInicial by InvestimentoTable.dataInicial
    var dataFinal by InvestimentoTable.dataFinal

    fun toDTO() = InvestimentoDTO(
        usuarioId = usuarioId.value, 
        valorInvestido = valorInvestido,
        taxa = taxa,
        dataInicial = dataInicial,
        dataFinal = dataFinal
    )
}

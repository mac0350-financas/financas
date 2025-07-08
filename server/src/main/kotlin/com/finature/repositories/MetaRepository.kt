package com.finature.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.ResultRow
import com.finature.models.TransacaoDTO
import com.finature.models.MetaDTO
import com.finature.db.dao.MetaDAO
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.TransacaoTable
import com.finature.db.tables.MetaTable
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.dao.id.EntityID

class MetaRepository {

    fun salvaMeta(meta: MetaDTO) {
        transaction {
            MetaDAO.new {
                usuarioId = EntityID(meta.usuarioId!!, UsuarioTable)
                categoria = meta.categoria
                valorLimite = meta.valorLimite
                valorAtual = meta.valorAtual
                dataInicial = meta.dataInicial
                dataFinal = meta.dataFinal
                tipoId = EntityID(meta.tipoId!!, TipoTransacaoTable)
            }
        }
    }

    fun listaMetasPorUsuario(usuarioId: Int, tipo: Int): List<MetaDTO> {
        return transaction {
            MetaTable.select {
                (MetaTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                (MetaTable.tipoId eq EntityID(tipo, TipoTransacaoTable))
            }.map { 
                MetaDTO(
                    usuarioId = it[MetaTable.usuarioId].value,
                    categoria = it[MetaTable.categoria],
                    valorLimite = it[MetaTable.valorLimite],
                    valorAtual = it[MetaTable.valorAtual],
                    dataInicial = it[MetaTable.dataInicial],
                    dataFinal = it[MetaTable.dataFinal],
                    tipoId = it[MetaTable.tipoId].value
                )
            }
        }
    }
}
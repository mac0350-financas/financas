package com.finature.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.ResultRow
import com.finature.models.TransacaoDTO
import com.finature.db.dao.TransacaoDAO
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.TransacaoTable
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.dao.id.EntityID

class TransacaoRepository {

    fun salvaTransacao(transacao: TransacaoDTO) {
        transaction {
            TransacaoDAO.new {
                usuarioId = EntityID(transacao.usuarioId!!, UsuarioTable)
                data = transacao.data
                descricao = transacao.descricao
                valor = transacao.valor
                tipoId = EntityID(transacao.tipoId!!, TipoTransacaoTable)
                categoria = transacao.categoria 
            }
        }
    }

    private fun dataFiltrada(mes: String, ano: String): Op<Boolean> {
        return when {
            mes == "0" && ano == "Todos" -> Op.TRUE
            mes == "0" -> TransacaoTable.data.like("%/$ano")
            ano == "Todos" -> TransacaoTable.data.like("%/${mes.padStart(2, '0')}/%")
            else -> TransacaoTable.data.like("%/${mes.padStart(2, '0')}/$ano")
        }
    }

    fun buscaGastosPorMesAno(usuarioId: Int, mes: String, ano: String): List<ResultRow> {
        return transaction {
            TransacaoTable.select {
                (TransacaoTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                (TransacaoTable.tipoId eq EntityID(1, TipoTransacaoTable)) and 
                dataFiltrada(mes, ano)
            }.toList()
        }
    }

    fun buscaReceitasPorMesAno(usuarioId: Int, mes: String, ano: String): List<ResultRow> {
        return transaction {
            TransacaoTable.select {
                (TransacaoTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                (TransacaoTable.tipoId eq EntityID(1, TipoTransacaoTable)) and 
                dataFiltrada(mes, ano)
            }.toList()
        }
    }

}
package com.finature.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import com.finature.models.TransacaoDTO
import com.finature.db.dao.TransacaoDAO
import com.finature.db.tables.UsuarioTable
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

}
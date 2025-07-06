package com.finature.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.sum
import com.finature.models.TransacaoDTO
import com.finature.models.CategoriaTotal
import com.finature.db.dao.TransacaoDAO
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.TransacaoTable
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.dao.id.EntityID

class TransacaoRepository {
    
    private fun dataFiltrada(mes: String, ano: String): Op<Boolean> {
        return when {
            mes == "0" && ano == "Todos" -> Op.TRUE
            mes == "0" -> TransacaoTable.data.like("$ano-%")
            ano == "Todos" -> TransacaoTable.data.like("____-${mes.padStart(2, '0')}-%")
            else -> TransacaoTable.data.like("$ano-${mes.padStart(2, '0')}-%")
        }
    }

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

    fun buscaGastosPorMesAno(usuarioId: Int, mes: String, ano: String): List<TransacaoDTO> {
        return transaction {
            TransacaoTable.select {
                (TransacaoTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                (TransacaoTable.tipoId eq EntityID(-1, TipoTransacaoTable)) and 
                dataFiltrada(mes, ano)
            }.map {
                TransacaoDTO(
                    data = it[TransacaoTable.data],
                    valor = it[TransacaoTable.valor],
                    tipoId = -1,
                    categoria = it[TransacaoTable.categoria],
                    descricao = it[TransacaoTable.descricao],
                    usuarioId = usuarioId
                )
            }
        }
    }

    fun buscaGastosPorCategoria(usuarioId: Int, mes: String, ano: String): List<CategoriaTotal> {
        val totalAlias = TransacaoTable.valor.sum()
        return transaction {
            TransacaoTable
                .slice(TransacaoTable.categoria, totalAlias)
                .select {
                    (TransacaoTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                    (TransacaoTable.tipoId eq EntityID(-1, TipoTransacaoTable)) and
                    dataFiltrada(mes, ano)
                }
                .groupBy(TransacaoTable.categoria)
                .map {
                    CategoriaTotal(
                        categoria = it[TransacaoTable.categoria],
                        total = it[totalAlias] ?: 0.0
                    )
                }
        }
    }
    
    
    
    fun buscaReceitasPorMesAno(usuarioId: Int, mes: String, ano: String): List<TransacaoDTO> {
        return transaction {
            TransacaoTable.select {
                (TransacaoTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                (TransacaoTable.tipoId eq EntityID(1, TipoTransacaoTable)) and 
                dataFiltrada(mes, ano)
            }.map {
                TransacaoDTO(
                    data = it[TransacaoTable.data],
                    valor = it[TransacaoTable.valor],
                    tipoId = 1,
                    categoria = it[TransacaoTable.categoria],
                    descricao = it[TransacaoTable.descricao],
                    usuarioId = usuarioId
                )
            }
        }
    }

    fun buscaReceitasPorCategoria(usuarioId: Int, mes: String, ano: String): List<CategoriaTotal> {
        val totalAlias = TransacaoTable.valor.sum()
        return transaction {
            TransacaoTable
                .slice(TransacaoTable.categoria, totalAlias)
                .select {
                    (TransacaoTable.usuarioId eq EntityID(usuarioId, UsuarioTable)) and
                    (TransacaoTable.tipoId eq EntityID(1, TipoTransacaoTable)) and
                    dataFiltrada(mes, ano)
                }
                .groupBy(TransacaoTable.categoria)
                .map {
                    CategoriaTotal(
                        categoria = it[TransacaoTable.categoria],
                        total = it[totalAlias] ?: 0.0
                    )
                }
        }
    }

}
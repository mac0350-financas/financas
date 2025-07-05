package com.finature.services

import com.finature.repositories.TransacaoRepository
import com.finature.models.TransacaoDTO
import com.finature.db.tables.TransacaoTable
import org.jetbrains.exposed.sql.ResultRow

class TransacaoService(private val transacaoRepository: TransacaoRepository) {

    fun criarTransacao(usuarioId: Int?, descricao: String, valor: Double, categoria: String, data: String, tipoId: Int) {
        val transacao = TransacaoDTO(
            data = data, 
            valor = valor, 
            tipoId = tipoId, 
            categoria = categoria, 
            descricao = descricao, 
            usuarioId = usuarioId
        )
        transacaoRepository.salvaTransacao(transacao) 
    }

    fun somaTransacoes(usuarioId: Int, tipo: Int, mes: String, ano: String): Double {
        val transacoes: List<ResultRow>
        if (tipo == -1) transacoes = transacaoRepository.buscaGastosPorMesAno(usuarioId, mes, ano)
        else transacoes = transacaoRepository.buscaReceitasPorMesAno(usuarioId, mes, ano)
        return transacoes.sumOf { it[TransacaoTable.valor] }
    }

}
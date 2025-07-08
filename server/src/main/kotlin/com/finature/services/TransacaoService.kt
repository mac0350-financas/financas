package com.finature.services

import com.finature.repositories.TransacaoRepository
import com.finature.models.TransacaoDTO
import com.finature.models.CategoriaTotal
import com.finature.db.tables.TransacaoTable
import org.jetbrains.exposed.sql.ResultRow

class TransacaoService(private val transacaoRepository: TransacaoRepository) {

    fun criarTransacao(usuarioId: Int?, descricao: String, valor: Double, categoria: String, data: String, tipoId: Int) {
        val transacao = TransacaoDTO(
            id = 0,
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
        val transacoes: List<TransacaoDTO> = if (tipo == -1)
            transacaoRepository.buscaGastosPorMesAno(usuarioId, mes, ano)
        else
            transacaoRepository.buscaReceitasPorMesAno(usuarioId, mes, ano)
    
        return transacoes.sumOf { it.valor }
    }
    

    fun listaTransacoes(usuarioId: Int, tipo: Int, mes: String, ano: String): List<TransacaoDTO> {
        val transacoes: List<TransacaoDTO> = if (tipo == -1)
            transacaoRepository.buscaGastosPorMesAno(usuarioId, mes, ano)
        else
            transacaoRepository.buscaReceitasPorMesAno(usuarioId, mes, ano)
        return transacoes
    }

    fun transacoesPorCategoria(usuarioId: Int, tipo: Int, mes: String, ano: String): List<CategoriaTotal> {
        if (tipo == -1)
            return transacaoRepository.buscaGastosPorCategoria(usuarioId, mes, ano)
        else
            return transacaoRepository.buscaReceitasPorCategoria(usuarioId, mes, ano)
    }

    fun somaTransacoesMeta(
        usuarioId: Int,
        tipoId: Int,
        categoria: String,
        dataInicial: String,
        dataFinal: String
    ): Double {
        val transacoes = transacaoRepository.buscaTransacoesMeta(usuarioId, tipoId, categoria, dataInicial, dataFinal)
        return transacoes.sumOf { it.valor }
    }

    fun removerTransacao(id: Int): Boolean {
        return transacaoRepository.deletarPorId(id)
    }
    

}
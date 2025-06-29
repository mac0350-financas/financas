package com.finature.services

import com.finature.repositories.TransacaoRepository
import com.finature.models.TransacaoDTO

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

}
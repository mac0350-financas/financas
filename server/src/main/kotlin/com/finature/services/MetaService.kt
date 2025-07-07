package com.finature.services

import com.finature.repositories.MetaRepository
import com.finature.models.MetaDTO
import com.finature.db.tables.MetaTable

class MetaService(private val metaRepository: MetaRepository) {

    fun criarMeta(
        usuarioId: Int,
        categoria: String,
        valorLimite: Double,
        valorAtual: Double,
        dataInicial: String,
        dataFinal: String,
        tipoId: Int
    ) {
        val meta = MetaDTO(
            usuarioId = usuarioId,
            categoria = categoria,
            valorLimite = valorLimite,
            valorAtual = valorAtual,
            dataInicial = dataInicial,
            dataFinal = dataFinal,
            tipoId = tipoId
        )
        metaRepository.salvaMeta(meta)
    }

    fun listaMetas(usuarioId: Int, tipo: Int): List<MetaDTO> {
        return metaRepository.listaMetasPorUsuario(usuarioId, tipo)
    }

}
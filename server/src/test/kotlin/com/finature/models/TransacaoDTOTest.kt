package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.idParam

class TransacaoDTOTest {

    @Test
    fun `deve criar TransacaoDTO corretamente`() {
        val transacao = TransacaoDTO(
            id = 0,
            data = "2024-01-15",
            valor = 250.0,
            tipoId = 1,
            categoria = "Transporte",
            descricao = "Uber",
            usuarioId = 42
        )

        assertEquals("2024-01-15", transacao.data)
        assertEquals(250.0, transacao.valor)
        assertEquals(1, transacao.tipoId)
        assertEquals("Transporte", transacao.categoria)
        assertEquals("Uber", transacao.descricao)
        assertEquals(42, transacao.usuarioId)
    }

    @Test
    fun `deve serializar e desserializar TransacaoDTO corretamente`() {
        val transacao = TransacaoDTO(
            id = 0,
            data = "2024-01-15",
            valor = 250.0,
            tipoId = 1,
            categoria = "Transporte",
            descricao = "Uber",
            usuarioId = 42
        )

        val json = Json.encodeToString(TransacaoDTO.serializer(), transacao)
        val transacaoDesserializada = Json.decodeFromString(TransacaoDTO.serializer(), json)

        assertEquals(transacao, transacaoDesserializada)
    }
}

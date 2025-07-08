package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class TipoTransacaoTest {

    @Test
    fun `deve criar TipoTransacaoDTO corretamente`() {
        val tipoTransacao = TipoTransacaoDTO(
            tipo = 1
        )

        assertEquals(1, tipoTransacao.tipo)
    }

    @Test
    fun `deve serializar e desserializar TipoTransacaoDTO corretamente`() {
        val tipoTransacao = TipoTransacaoDTO(
            tipo = 1
        )

        val json = Json.encodeToString(TipoTransacaoDTO.serializer(), tipoTransacao)
        val tipoTransacaoDesserializado = Json.decodeFromString(TipoTransacaoDTO.serializer(), json)

        assertEquals(tipoTransacao, tipoTransacaoDesserializado)
    }
}
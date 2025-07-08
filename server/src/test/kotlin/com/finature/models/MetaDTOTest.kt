package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class MetaDTOTest {

    @Test
    fun `deve criar MetaDTO corretamente`() {
        val meta = MetaDTO(
            usuarioId = 1,
            categoria = "Alimentação",
            valorLimite = 500.0,
            valorAtual = 100.0,
            dataInicial = "2024-01-01",
            dataFinal = "2024-01-31",
            tipoId = -1
        )

        assertEquals(1, meta.usuarioId)
        assertEquals("Alimentação", meta.categoria)
        assertEquals(500.0, meta.valorLimite)
        assertEquals(100.0, meta.valorAtual)
        assertEquals("2024-01-01", meta.dataInicial)
        assertEquals("2024-01-31", meta.dataFinal)
        assertEquals(-1, meta.tipoId)
    }

    @Test
    fun `deve serializar e desserializar MetaDTO corretamente`() {
        val meta = MetaDTO(
            usuarioId = 1,
            categoria = "Alimentação",
            valorLimite = 500.0,
            valorAtual = 100.0,
            dataInicial = "2024-01-01",
            dataFinal = "2024-01-31",
            tipoId = -1
        )

        val json = Json.encodeToString(MetaDTO.serializer(), meta)
        val metaDesserializado = Json.decodeFromString(MetaDTO.serializer(), json)

        assertEquals(meta, metaDesserializado)
    }
}

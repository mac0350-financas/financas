package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class CategoriaTotalTest {

    @Test
    fun `deve criar CategoriaTotal corretamente`() {
        val categoriaTotal = CategoriaTotal(
            categoria = "Alimentação",
            total = 500.0
        )

        assertEquals("Alimentação", categoriaTotal.categoria)
        assertEquals(500.0, categoriaTotal.total)
    }

    @Test
    fun `deve serializar e desserializar CategoriaTotal corretamente`() {
        val categoriaTotal = CategoriaTotal(
            categoria = "Alimentação",
            total = 500.0
        )

        val json = Json.encodeToString(CategoriaTotal.serializer(), categoriaTotal)
        val categoriaTotalDesserializado = Json.decodeFromString(CategoriaTotal.serializer(), json)

        assertEquals(categoriaTotal, categoriaTotalDesserializado)
    }
}
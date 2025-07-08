package com.finature.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class UsuarioDTOTest {

    @Test
    fun `deve criar UsuarioDTO corretamente`() {
        val usuario = UsuarioDTO(
            id = 1,
            nome = "Alice",
            email = "alice@example.com",
            senha = "senhaSegura"
        )

        assertEquals(1, usuario.id)
        assertEquals("Alice", usuario.nome)
        assertEquals("alice@example.com", usuario.email)
        assertEquals("senhaSegura", usuario.senha)
    }

    @Test
    fun `deve serializar e desserializar UsuarioDTO corretamente`() {
        val usuario = UsuarioDTO(
            id = 1,
            nome = "Alice",
            email = "alice@example.com",
            senha = "senhaSegura"
        )

        val json = Json.encodeToString(UsuarioDTO.serializer(), usuario)
        val usuarioDesserializado = Json.decodeFromString(UsuarioDTO.serializer(), json)

        assertEquals(usuario, usuarioDesserializado)
    }
}
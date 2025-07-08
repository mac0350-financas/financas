package com.finature.sessions

import kotlin.test.Test
import kotlin.test.assertEquals

class UsuarioSessaoTest {

    @Test
    fun `deve acessar os campos de UsuarioSessao`() {
        val sessao = UsuarioSessao(
            id = 1,
            nome = "Muriel",
            email = "muriel@email.com"
        )

        // Acesso direto às propriedades (o bytecode usa getX())
        assertEquals(1, sessao.id)
        assertEquals("Muriel", sessao.nome)
        assertEquals("muriel@email.com", sessao.email)
    }
}

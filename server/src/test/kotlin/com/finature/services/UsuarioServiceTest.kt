package com.finature.services

import com.finature.models.UsuarioDTO
import com.finature.repositories.UsuarioRepository
import io.mockk.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Testes unitários para [UsuarioService].
 *
 * A estratégia aqui é isolar a lógica de negócio do serviço "fingindo" que o
 * repositório já está testado (ou será coberto por testes de integração).
 * Usamos **MockK** para criar um mock do [UsuarioRepository] e verificar as
 * interações esperadas.
 */

class UsuarioServiceTest {

    // ‑‑‑ fixtures -----------------------------------------------------------
    private lateinit var repository: UsuarioRepository
    private lateinit var service: UsuarioService

    @BeforeTest
    fun setUp() {
        repository = mockk(relaxed = true) // cria mock "solto" para Unit
        service = UsuarioService(repository)
    }

    // ‑‑‑ criarConta() -------------------------------------------------------

    @Test
    fun `deve criar usuario quando email nao existe`() {
        every { repository.verificaEmailExistente("ana@exemplo.com") } returns null
        justRun { repository.salvaUsuario(any()) }

        service.criarConta("Ana", "ana@exemplo.com", "123")

        verify(exactly = 1) {
            repository.salvaUsuario(match {
                it.nome == "Ana" && it.email == "ana@exemplo.com" && it.senha == "123"
            })
        }
    }

    @Test
    fun `deve lançar excecao quando email ja existe`() {
        every { repository.verificaEmailExistente("ana@exemplo.com") } returns UsuarioDTO(
            id = 1,
            nome = "Ana",
            email = "ana@exemplo.com",
            senha = "123"
        )

        val erro = assertFailsWith<Exception> {
            service.criarConta("Ana", "ana@exemplo.com", "novaSenha")
        }
        assertEquals("Email já está em uso", erro.message)
        verify(exactly = 0) { repository.salvaUsuario(any()) }
    }

    // ‑‑‑ fazerLogin() -------------------------------------------------------

    @Test
    fun `login deve retornar usuario quando credenciais corretas`() {
        val usuario = UsuarioDTO(1, "Ana", "ana@exemplo.com", "123")
        every { repository.verificaEmailExistente("ana@exemplo.com") } returns usuario

        val resultado = service.fazerLogin("ana@exemplo.com", "123")
        assertEquals(usuario, resultado)
    }

    @Test
    fun `login deve falhar quando email nao encontrado`() {
        every { repository.verificaEmailExistente("nao@existe.com") } returns null

        val erro = assertFailsWith<Exception> {
            service.fazerLogin("nao@existe.com", "123")
        }
        assertEquals("Email não encontrado", erro.message)
    }

    @Test
    fun `login deve falhar quando senha incorreta`() {
        val usuario = UsuarioDTO(1, "Ana", "ana@exemplo.com", "123")
        every { repository.verificaEmailExistente("ana@exemplo.com") } returns usuario

        val erro = assertFailsWith<Exception> {
            service.fazerLogin("ana@exemplo.com", "999")
        }
        assertEquals("Senha incorreta", erro.message)
    }
}

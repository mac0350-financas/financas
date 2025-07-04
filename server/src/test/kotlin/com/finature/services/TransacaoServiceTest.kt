package com.finature.services

import com.finature.repositories.TransacaoRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransacaoServiceTest {

    private lateinit var transacaoRepository: TransacaoRepository
    private lateinit var transacaoService: TransacaoService

    @BeforeEach
    fun setup() {
        transacaoRepository = mockk()
        transacaoService = TransacaoService(transacaoRepository)
    }

    @Test
    fun `criarTransacao deve salvar transacao com dados corretos`() {
        val usuarioId = 1
        val descricao = "Compra no supermercado"
        val valor = 150.75
        val categoria = "Alimentação"
        val data = "2024-01-15"
        val tipoId = 2
        
        every { transacaoRepository.salvaTransacao(any()) } just Runs

        transacaoService.criarTransacao(usuarioId, descricao, valor, categoria, data, tipoId)

        verify { 
            transacaoRepository.salvaTransacao(match { transacao ->
                transacao.usuarioId == usuarioId &&
                transacao.descricao == descricao &&
                transacao.valor == valor &&
                transacao.categoria == categoria &&
                transacao.data == data &&
                transacao.tipoId == tipoId
            })
        }
    }

    @Test
    fun `criarTransacao deve funcionar com valores decimais precisos`() {
        // dado
        val usuarioId = 1
        val descricao = "Transferência PIX"
        val valor = 1234.56
        val categoria = "Transferência"
        val data = "2024-01-15"
        val tipoId = 1
        
        every { transacaoRepository.salvaTransacao(any()) } just Runs

        // quando
        transacaoService.criarTransacao(usuarioId, descricao, valor, categoria, data, tipoId)

        // entao
        verify { 
            transacaoRepository.salvaTransacao(match { it.valor == 1234.56 })
        }
    }
}

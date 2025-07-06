package com.finature.services

import kotlin.test.*
import io.mockk.*
import com.finature.repositories.TransacaoRepository
import com.finature.models.TransacaoDTO
import com.finature.models.CategoriaTotal

/**
 * Testes unitários para TransacaoService.
 * Usa mocks para isolar a lógica de negócio.
 */
class TransacaoServiceTest {

    private val mockRepository = mockk<TransacaoRepository>()
    private val service = TransacaoService(mockRepository)

    @Test
    fun `criarTransacao chama repository com dados corretos`() {
        every { mockRepository.salvaTransacao(any()) } just Runs
        
        service.criarTransacao(1, "Teste", 100.0, "Categoria", "2024-01-01", -1)
        
        verify {
            mockRepository.salvaTransacao(
                TransacaoDTO(
                    data = "2024-01-01",
                    valor = 100.0,
                    tipoId = -1,
                    categoria = "Categoria",
                    descricao = "Teste",
                    usuarioId = 1
                )
            )
        }
    }

    @Test
    fun `somaTransacoes calcula total de gastos corretamente`() {
        val transacoes = listOf(
            TransacaoDTO("2024-01-01", 100.0, -1, "Cat1", "Desc1", 1),
            TransacaoDTO("2024-01-02", 150.0, -1, "Cat2", "Desc2", 1)
        )
        every { mockRepository.buscaGastosPorMesAno(1, "01", "2024") } returns transacoes
        
        val total = service.somaTransacoes(1, -1, "01", "2024")
        
        assertEquals(250.0, total)
    }

    @Test
    fun `somaTransacoes calcula total de receitas corretamente`() {
        val transacoes = listOf(
            TransacaoDTO("2024-01-01", 2000.0, 1, "Salário", "Janeiro", 1)
        )
        every { mockRepository.buscaReceitasPorMesAno(1, "01", "2024") } returns transacoes
        
        val total = service.somaTransacoes(1, 1, "01", "2024")
        
        assertEquals(2000.0, total)
    }

    @Test
    fun `listaTransacoes retorna gastos quando tipo eh -1`() {
        val gastos = listOf(TransacaoDTO("2024-01-01", 100.0, -1, "Cat", "Desc", 1))
        every { mockRepository.buscaGastosPorMesAno(1, "01", "2024") } returns gastos
        
        val resultado = service.listaTransacoes(1, -1, "01", "2024")
        
        assertEquals(gastos, resultado)
        verify { mockRepository.buscaGastosPorMesAno(1, "01", "2024") }
    }

    @Test
    fun `transacoesPorCategoria retorna categorias de gastos`() {
        val categorias = listOf(CategoriaTotal("Alimentação", 150.0))
        every { mockRepository.buscaGastosPorCategoria(1, "01", "2024") } returns categorias
        
        val resultado = service.transacoesPorCategoria(1, -1, "01", "2024")
        
        assertEquals(categorias, resultado)
    }
}

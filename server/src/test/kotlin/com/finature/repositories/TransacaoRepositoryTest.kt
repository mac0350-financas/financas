package com.finature.repositories

import kotlin.test.*
import org.junit.jupiter.api.assertThrows

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.TransacaoTable
import com.finature.db.tables.TipoTransacaoTable
import com.finature.db.dao.UsuarioDAO
import com.finature.db.dao.TipoTransacaoDAO


import com.finature.models.TransacaoDTO
import com.finature.models.UsuarioDTO

/**
 * Testes de integração (nível repositório) para TransacaoRepository.
 *
 * ▸ Usa H2 em memória; cada teste recria as tabelas para isolar estado.  
 * ▸ Não há mocks – validamos a persistência real com Exposed.
 */
class TransacaoRepositoryTest {

    private lateinit var repo: TransacaoRepository
    private lateinit var usuarioRepo: UsuarioRepository
    private var usuarioId: Int = 0

    @BeforeTest
    fun setupDb() {
        // 1 – conecta a banco H2 que vive só na JVM de teste
        Database.connect("jdbc:h2:mem:transacaoRepo;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // 2 – cria as tabelas necessárias
        transaction {
            SchemaUtils.drop(TransacaoTable, TipoTransacaoTable, UsuarioTable)
            SchemaUtils.create(UsuarioTable, TipoTransacaoTable, TransacaoTable)
            
            // 3 – insere dados básicos para os testes
            // Tipos de transação
            TipoTransacaoDAO.new(-1) { tipo = -1 }  // Gasto
            TipoTransacaoDAO.new(1) { tipo = 1 }    // Receita
            
            // Usuário de teste
            val usuario = UsuarioDAO.new {
                nome = "Usuario Teste"
                email = "teste@teste.com"
                senha = "senha123"
            }
            usuarioId = usuario.id.value
        }

        repo = TransacaoRepository()
        usuarioRepo = UsuarioRepository()
    }

    /* ---------- CASOS DE TESTE ---------- */

    @Test
    fun `salvaTransacao persiste gasto corretamente`() {
        val transacao = TransacaoDTO(
            data = "2024-01-15",
            valor = 100.0,
            tipoId = -1,
            categoria = "Alimentação",
            descricao = "Almoço",
            usuarioId = usuarioId
        )
        
        repo.salvaTransacao(transacao)
        
        val gastos = repo.buscaGastosPorMesAno(usuarioId, "01", "2024")
        assertEquals(1, gastos.size)
        assertEquals(100.0, gastos[0].valor)
        assertEquals("Alimentação", gastos[0].categoria)
        assertEquals("Almoço", gastos[0].descricao)
    }

    @Test
    fun `salvaTransacao persiste receita corretamente`() {
        val transacao = TransacaoDTO(
            data = "2024-01-15",
            valor = 2000.0,
            tipoId = 1,
            categoria = "Salário",
            descricao = "Salário Janeiro",
            usuarioId = usuarioId
        )
        
        repo.salvaTransacao(transacao)
        
        val receitas = repo.buscaReceitasPorMesAno(usuarioId, "01", "2024")
        assertEquals(1, receitas.size)
        assertEquals(2000.0, receitas[0].valor)
        assertEquals("Salário", receitas[0].categoria)
    }

    @Test
    fun `buscaGastosPorMesAno filtra corretamente por mes e ano`() {
        // Adiciona gastos em meses diferentes
        repo.salvaTransacao(TransacaoDTO("2024-01-15", 100.0, -1, "Alimentação", "Almoço", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-02-15", 200.0, -1, "Transporte", "Uber", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2023-01-15", 300.0, -1, "Alimentação", "Jantar", usuarioId))
        
        val gastosJan2024 = repo.buscaGastosPorMesAno(usuarioId, "01", "2024")
        assertEquals(1, gastosJan2024.size)
        assertEquals(100.0, gastosJan2024[0].valor)
        
        val gastosFev2024 = repo.buscaGastosPorMesAno(usuarioId, "02", "2024")
        assertEquals(1, gastosFev2024.size)
        assertEquals(200.0, gastosFev2024[0].valor)
    }

    @Test
    fun `buscaReceitasPorMesAno filtra corretamente por mes e ano`() {
        // Adiciona receitas em meses diferentes
        repo.salvaTransacao(TransacaoDTO("2024-01-01", 2000.0, 1, "Salário", "Salário Janeiro", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-02-01", 2100.0, 1, "Salário", "Salário Fevereiro", usuarioId))
        
        val receitasJan = repo.buscaReceitasPorMesAno(usuarioId, "01", "2024")
        assertEquals(1, receitasJan.size)
        assertEquals(2000.0, receitasJan[0].valor)
    }

    @Test
    fun `buscaGastosPorCategoria agrupa e soma corretamente`() {
        // Adiciona múltiplos gastos na mesma categoria
        repo.salvaTransacao(TransacaoDTO("2024-01-01", 50.0, -1, "Alimentação", "Café", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-01-02", 100.0, -1, "Alimentação", "Almoço", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-01-03", 200.0, -1, "Transporte", "Uber", usuarioId))
        
        val categorias = repo.buscaGastosPorCategoria(usuarioId, "01", "2024")
        assertEquals(2, categorias.size)
        
        val alimentacao = categorias.find { it.categoria == "Alimentação" }
        val transporte = categorias.find { it.categoria == "Transporte" }
        
        assertNotNull(alimentacao)
        assertNotNull(transporte)
        assertEquals(150.0, alimentacao.total)
        assertEquals(200.0, transporte.total)
    }

    @Test
    fun `buscaReceitasPorCategoria agrupa e soma corretamente`() {
        repo.salvaTransacao(TransacaoDTO("2024-01-01", 2000.0, 1, "Salário", "Salário principal", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-01-15", 500.0, 1, "Freelance", "Projeto extra", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-01-20", 300.0, 1, "Freelance", "Outro projeto", usuarioId))
        
        val categorias = repo.buscaReceitasPorCategoria(usuarioId, "01", "2024")
        assertEquals(2, categorias.size)
        
        val freelance = categorias.find { it.categoria == "Freelance" }
        val salario = categorias.find { it.categoria == "Salário" }
        
        assertNotNull(freelance)
        assertNotNull(salario)
        assertEquals(800.0, freelance.total)
        assertEquals(2000.0, salario.total)
    }

    @Test
    fun `busca retorna lista vazia quando nao ha transacoes`() {
        val gastos = repo.buscaGastosPorMesAno(usuarioId, "12", "2024")
        val receitas = repo.buscaReceitasPorMesAno(usuarioId, "12", "2024")
        val categoriasGasto = repo.buscaGastosPorCategoria(usuarioId, "12", "2024")
        val categoriasReceita = repo.buscaReceitasPorCategoria(usuarioId, "12", "2024")
        
        assertTrue(gastos.isEmpty())
        assertTrue(receitas.isEmpty())
        assertTrue(categoriasGasto.isEmpty())
        assertTrue(categoriasReceita.isEmpty())
    }

    @Test
    fun `filtro por ano funciona corretamente`() {
        repo.salvaTransacao(TransacaoDTO("2023-06-15", 100.0, -1, "Alimentação", "Teste 2023", usuarioId))
        repo.salvaTransacao(TransacaoDTO("2024-06-15", 200.0, -1, "Alimentação", "Teste 2024", usuarioId))
        
        val gastos2023 = repo.buscaGastosPorMesAno(usuarioId, "0", "2023")
        val gastos2024 = repo.buscaGastosPorMesAno(usuarioId, "0", "2024")
        
        assertEquals(1, gastos2023.size)
        assertEquals(1, gastos2024.size)
        assertEquals(100.0, gastos2023[0].valor)
        assertEquals(200.0, gastos2024[0].valor)
    }
}

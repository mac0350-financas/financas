package com.finature.repositories

import kotlin.test.*
import org.junit.jupiter.api.assertThrows

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.MetaTable
import com.finature.db.tables.TipoTransacaoTable
import com.finature.db.dao.UsuarioDAO
import com.finature.db.dao.TipoTransacaoDAO

import com.finature.models.MetaDTO

/**
 * Testes de integração (nível repositório) para MetaRepository.
 *
 * ▸ Usa H2 em memória; cada teste recria as tabelas para isolar estado.  
 * ▸ Não há mocks – validamos a persistência real com Exposed.
 */
class MetaRepositoryTest {

    private lateinit var repo: MetaRepository
    private var usuarioId: Int = 0

    @BeforeTest
    fun setupDb() {
        // 1 – conecta a banco H2 que vive só na JVM de teste
        Database.connect("jdbc:h2:mem:metaRepo;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // 2 – cria as tabelas necessárias
        transaction {
            SchemaUtils.drop(MetaTable, TipoTransacaoTable, UsuarioTable)
            SchemaUtils.create(UsuarioTable, TipoTransacaoTable, MetaTable)
            
            // 3 – insere dados básicos para os testes
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

        repo = MetaRepository()
    }

    /* ---------- CASOS DE TESTE ---------- */

    @Test
    fun `salvaMeta persiste meta de gasto corretamente`() {
        val meta = MetaDTO(
            usuarioId = usuarioId,
            categoria = "Alimentação",
            valorLimite = 500.0,
            valorAtual = 150.0,
            dataInicial = "2024-01-01",
            dataFinal = "2024-01-31",
            tipoId = -1
        )
        
        repo.salvaMeta(meta)
        
        val metas = repo.listaMetasPorUsuario(usuarioId, -1)
        assertEquals(1, metas.size)
        assertEquals("Alimentação", metas[0].categoria)
        assertEquals(500.0, metas[0].valorLimite)
        assertEquals(150.0, metas[0].valorAtual)
        assertEquals("2024-01-01", metas[0].dataInicial)
        assertEquals("2024-01-31", metas[0].dataFinal)
        assertEquals(-1, metas[0].tipoId)
    }

    @Test
    fun `salvaMeta persiste meta de receita corretamente`() {
        val meta = MetaDTO(
            usuarioId = usuarioId,
            categoria = "Salário",
            valorLimite = 3000.0,
            valorAtual = 3000.0,
            dataInicial = "2024-01-01",
            dataFinal = "2024-01-31",
            tipoId = 1
        )
        
        repo.salvaMeta(meta)
        
        val metas = repo.listaMetasPorUsuario(usuarioId, 1)
        assertEquals(1, metas.size)
        assertEquals("Salário", metas[0].categoria)
        assertEquals(3000.0, metas[0].valorLimite)
        assertEquals(1, metas[0].tipoId)
    }

    @Test
    fun `listaMetasPorUsuario filtra corretamente por tipo`() {
        // Adiciona metas de tipos diferentes
        val metaGasto = MetaDTO(usuarioId, "Alimentação", 500.0, 100.0, "2024-01-01", "2024-01-31", -1)
        val metaReceita = MetaDTO(usuarioId, "Salário", 3000.0, 2000.0, "2024-01-01", "2024-01-31", 1)
        
        repo.salvaMeta(metaGasto)
        repo.salvaMeta(metaReceita)
        
        val metasGasto = repo.listaMetasPorUsuario(usuarioId, -1)
        val metasReceita = repo.listaMetasPorUsuario(usuarioId, 1)
        
        assertEquals(1, metasGasto.size)
        assertEquals(1, metasReceita.size)
        assertEquals("Alimentação", metasGasto[0].categoria)
        assertEquals("Salário", metasReceita[0].categoria)
    }

    @Test
    fun `listaMetasPorUsuario retorna multiplas metas do mesmo tipo`() {
        val meta1 = MetaDTO(usuarioId, "Alimentação", 500.0, 100.0, "2024-01-01", "2024-01-31", -1)
        val meta2 = MetaDTO(usuarioId, "Transporte", 300.0, 80.0, "2024-01-01", "2024-01-31", -1)
        val meta3 = MetaDTO(usuarioId, "Lazer", 200.0, 50.0, "2024-01-01", "2024-01-31", -1)
        
        repo.salvaMeta(meta1)
        repo.salvaMeta(meta2)
        repo.salvaMeta(meta3)
        
        val metas = repo.listaMetasPorUsuario(usuarioId, -1)
        assertEquals(3, metas.size)
        
        val categorias = metas.map { it.categoria }.sorted()
        assertEquals(listOf("Alimentação", "Lazer", "Transporte"), categorias)
    }

    @Test
    fun `listaMetasPorUsuario retorna lista vazia quando nao ha metas`() {
        val metas = repo.listaMetasPorUsuario(usuarioId, -1)
        assertTrue(metas.isEmpty())
    }

    @Test
    fun `listaMetasPorUsuario retorna lista vazia para tipo inexistente`() {
        val meta = MetaDTO(usuarioId, "Alimentação", 500.0, 100.0, "2024-01-01", "2024-01-31", -1)
        repo.salvaMeta(meta)
        
        // Busca por tipo que não existe
        val metas = repo.listaMetasPorUsuario(usuarioId, 999)
        assertTrue(metas.isEmpty())
    }

    @Test
    fun `listaMetasPorUsuario filtra corretamente por usuario`() {
        // Cria segundo usuário
        val outroUsuarioId = transaction {
            UsuarioDAO.new {
                nome = "Outro Usuario"
                email = "outro@teste.com"
                senha = "senha456"
            }.id.value
        }
        
        val metaUsuario1 = MetaDTO(usuarioId, "Alimentação", 500.0, 100.0, "2024-01-01", "2024-01-31", -1)
        val metaUsuario2 = MetaDTO(outroUsuarioId, "Transporte", 300.0, 80.0, "2024-01-01", "2024-01-31", -1)
        
        repo.salvaMeta(metaUsuario1)
        repo.salvaMeta(metaUsuario2)
        
        val metasUsuario1 = repo.listaMetasPorUsuario(usuarioId, -1)
        val metasUsuario2 = repo.listaMetasPorUsuario(outroUsuarioId, -1)
        
        assertEquals(1, metasUsuario1.size)
        assertEquals(1, metasUsuario2.size)
        assertEquals("Alimentação", metasUsuario1[0].categoria)
        assertEquals("Transporte", metasUsuario2[0].categoria)
    }

    @Test
    fun `salvaMeta preserva todos os campos corretamente`() {
        val meta = MetaDTO(
            usuarioId = usuarioId,
            categoria = "Categoria Teste",
            valorLimite = 1234.56,
            valorAtual = 678.90,
            dataInicial = "2024-02-15",
            dataFinal = "2024-03-15",
            tipoId = -1
        )
        
        repo.salvaMeta(meta)
        
        val metasSalvas = repo.listaMetasPorUsuario(usuarioId, -1)
        assertEquals(1, metasSalvas.size)
        
        val metaSalva = metasSalvas[0]
        assertEquals(usuarioId, metaSalva.usuarioId)
        assertEquals("Categoria Teste", metaSalva.categoria)
        assertEquals(1234.56, metaSalva.valorLimite)
        assertEquals(678.90, metaSalva.valorAtual)
        assertEquals("2024-02-15", metaSalva.dataInicial)
        assertEquals("2024-03-15", metaSalva.dataFinal)
        assertEquals(-1, metaSalva.tipoId)
    }
}

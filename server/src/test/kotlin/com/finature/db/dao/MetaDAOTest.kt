package com.finature.db.dao

import com.finature.db.tables.MetaTable
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class MetaDAOTest {

    @BeforeTest
    fun setup() {
        Database.connect(
            "jdbc:h2:mem:test_${System.nanoTime()};MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(UsuarioTable, MetaTable, TipoTransacaoTable)
        }
    }

    @Test
    fun `inserir, consultar, atualizar e deletar MetaDAO`() {
        transaction {
            // Primeiro cria um usuário para referenciar (supondo que MetaTable.usuarioId é FK)
            val usuario = com.finature.db.dao.UsuarioDAO.new {
                nome = "Teste Usuario"
                email = "teste@usuario.com"
                senha = "senha123"
            }

            val tipoTransacao = com.finature.db.dao.TipoTransacaoDAO.new {
                tipo = 1
            }

            // Insere uma meta
            val meta = MetaDAO.new {
                usuarioId = usuario.id
                categoria = "Alimentação"
                valorLimite = 1000.0
                valorAtual = 100.0
                dataInicial = "2024-01-01"
                dataFinal = "2024-12-31"
                tipoId = tipoTransacao.id
            }

            // Consulta por id
            val consulta = MetaDAO.findById(meta.id)
            assertNotNull(consulta)
            assertEquals("Alimentação", consulta?.categoria)
            assertEquals(1000.0, consulta?.valorLimite)

            // Testa conversão para DTO
            val dto = consulta?.toDTO()
            assertNotNull(dto)
            assertEquals(meta.id.value, consulta.id.value) // Id confere
            assertEquals("Alimentação", dto?.categoria)
            assertEquals(100.0, dto?.valorAtual)

            // Atualiza valorAtual
            consulta.valorAtual = 200.0
            assertEquals(200.0, consulta.valorAtual)

            // Deleta o registro
            consulta.delete()
            assertNull(MetaDAO.findById(meta.id))
        }
    }
}

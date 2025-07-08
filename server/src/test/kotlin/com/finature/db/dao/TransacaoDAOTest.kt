package com.finature.db.dao

import com.finature.db.tables.TransacaoTable
import com.finature.db.tables.UsuarioTable
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class TransacaoDAOTest {

    @BeforeTest
    fun setup() {
        Database.connect(
            "jdbc:h2:mem:test_${System.nanoTime()};MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(UsuarioTable, TransacaoTable, TipoTransacaoTable)
        }
    }

    @Test
    fun `inserir, consultar, atualizar e deletar TransacaoDAO`() {
        transaction {
            // Primeiro cria um usuário para referenciar (supondo que TransacaoTable.usuarioId é FK)
            val usuario = UsuarioDAO.new {
                nome = "Teste Usuario"
                email = "teste@usuario.com"
                senha = "senha123"
            }

            val tipoTransacao = TipoTransacaoDAO.new {
                tipo = 1
            }

            // Insere uma transação
            val transacao = TransacaoDAO.new {
                usuarioId = usuario.id
                data = "2024-01-01"
                valor = 500.0
                tipoId = tipoTransacao.id
                categoria = "Educação"
                descricao = "Curso de Kotlin"
            }

            // Consulta por id
            val consulta = TransacaoDAO.findById(transacao.id)
            assertNotNull(consulta)
            assertEquals("Educação", consulta?.categoria)
            assertEquals(500.0, consulta?.valor)

            // Testa conversão para DTO
            val dto = consulta?.toDTO()
            assertNotNull(dto)
            assertEquals(transacao.id.value, consulta.id.value) // Id confere
            assertEquals("Educação", dto?.categoria)
            assertEquals("Curso de Kotlin", dto?.descricao)

            // Atualiza valor
            consulta.valor = 600.0
            assertEquals(600.0, consulta.valor)

            // Deleta o registro
            consulta.delete()
            assertNull(TransacaoDAO.findById(transacao.id))
        }
    }
}
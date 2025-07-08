package com.finature.db.dao

import com.finature.db.tables.InvestimentoTable
import com.finature.db.tables.UsuarioTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class InvestimentoDAOTest {

    @BeforeTest
    fun setup() {
        Database.connect(
            "jdbc:h2:mem:test_${System.nanoTime()};MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(UsuarioTable, InvestimentoTable)
        }
    }

    @Test
    fun `inserir, consultar, atualizar e deletar InvestimentoDAO`() {
        transaction {
            // Primeiro cria um usuário para referenciar (supondo que InvestimentoTable.usuarioId é FK)
            val usuario = UsuarioDAO.new {
                nome = "Teste Usuario"
                email = "teste@usuario.com"
                senha = "senha123"
            }

            // Insere um investimento
            val investimento = InvestimentoDAO.new {
                usuarioId = usuario.id
                valorInvestido = 5000.0
                taxa = 0.05
                dataInicial = "2024-01-01"
                dataFinal = "2024-12-31"
            }

            // Consulta por id
            val consulta = InvestimentoDAO.findById(investimento.id)
            assertNotNull(consulta)
            assertEquals(5000.0, consulta?.valorInvestido)
            assertEquals(0.05, consulta?.taxa)

            // Testa conversão para DTO
            val dto = consulta?.toDTO()
            assertNotNull(dto)
            assertEquals(investimento.id.value, consulta.id.value) // Id confere
            assertEquals(5000.0, dto?.valorInvestido)
            assertEquals(0.05, dto?.taxa)

            // Atualiza valorInvestido
            consulta.valorInvestido = 6000.0
            assertEquals(6000.0, consulta.valorInvestido)

            // Deleta o registro
            consulta.delete()
            assertNull(InvestimentoDAO.findById(investimento.id))
        }
    }
}
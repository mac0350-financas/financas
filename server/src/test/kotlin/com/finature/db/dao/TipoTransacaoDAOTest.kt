package com.finature.db.dao

import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class TipoTransacaoDAOTest {

    @BeforeTest
    fun setup() {
        Database.connect(
            "jdbc:h2:mem:test_${System.nanoTime()};MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(TipoTransacaoTable)
        }
    }

    @Test
    fun `inserir, consultar, atualizar e deletar TipoTransacaoDAO`() {
        transaction {
            // Insere um tipo de transação
            val tipoTransacao = TipoTransacaoDAO.new {
                tipo = 1
            }

            // Consulta por id
            val consulta = TipoTransacaoDAO.findById(tipoTransacao.id)
            assertNotNull(consulta)
            assertEquals(1, consulta?.tipo)

            // Testa conversão para DTO
            val dto = consulta?.toDTO()
            assertNotNull(dto)
            assertEquals(tipoTransacao.id.value, consulta.id.value) // Id confere
            assertEquals(1, dto?.tipo)

            // Atualiza o tipo
            consulta.tipo = 2
            assertEquals(2, consulta.tipo)

            // Deleta o registro
            consulta.delete()
            assertNull(TipoTransacaoDAO.findById(tipoTransacao.id))
        }
    }
}
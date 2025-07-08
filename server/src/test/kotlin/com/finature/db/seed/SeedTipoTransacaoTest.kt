package com.finature.db.seed

import com.finature.db.tables.*
import com.finature.db.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class SeedTipoTransacaoTest {

    @BeforeTest
    fun preparaBanco() {
        Database.connect(
            "jdbc:h2:mem:test_${System.nanoTime()};MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            // Cria as tabelas necessárias para o teste
            SchemaUtils.create(
                UsuarioTable,
                TipoTransacaoTable,
                TransacaoTable,
                MetaTable,
                InvestimentoTable
            )

            // Popula dados básicos
            UsuarioDAO.new {
                nome = "Usuário Teste"
                email = "teste@teste.com"
                senha = "123456"
            }
        }
    }

    @Test
    fun `seedTipoTransacao deve popular tabela TipoTransacaoDAO`() {
        transaction {
            assertEquals(0, TipoTransacaoDAO.all().count())

            seedTipoTransacao()

            val count = TipoTransacaoDAO.all().count()
            println("Tipos de transação criados: $count")
            assertEquals(2, count, "Esperado exatamente 2 tipos de transação após o seed")

            val tipoGasto = TipoTransacaoDAO.find { TipoTransacaoTable.tipo eq -1 }.singleOrNull()
            val tipoReceita = TipoTransacaoDAO.find { TipoTransacaoTable.tipo eq 1 }.singleOrNull()

            assertNotNull(tipoGasto)
            assertNotNull(tipoReceita)
        }
    }
}

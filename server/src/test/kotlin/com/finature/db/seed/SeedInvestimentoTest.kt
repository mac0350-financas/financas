package com.finature.db.seed

import com.finature.db.tables.*
import com.finature.db.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class SeedInvestimentoTest {

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

            TipoTransacaoDAO.new {
                tipo = 1
            }
            TipoTransacaoDAO.new {
                tipo = -1
            }
        }
    }

    @Test
    fun `seedInvestimento deve popular tabela InvestimentoDAO`() {
        transaction {
            assertEquals(0, InvestimentoDAO.all().count())

            seedInvestimento()

            val count = InvestimentoDAO.all().count()
            println("Investimentos criados: $count")
            assertTrue(count > 0, "Esperado pelo menos 1 investimento após o seed")

            val investimento = InvestimentoDAO.all().first()
            assertTrue(investimento.valorInvestido > 0)
        }
    }
}

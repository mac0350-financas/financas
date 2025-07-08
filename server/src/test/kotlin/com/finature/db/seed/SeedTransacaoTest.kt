package com.finature.db.seed

import com.finature.db.tables.*
import com.finature.db.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.test.*

class SeedTransacaoTest {

    @BeforeTest
    fun preparaBanco() {
        // Cria um banco H2 em memória temporário
        val dbName = "test_${System.nanoTime()}"
        Database.connect(
            "jdbc:h2:mem:$dbName;MODE=MariaDB;DB_CLOSE_DELAY=-1",
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
    fun `seedTransacao deve popular tabela TransacaoDAO`() {
        transaction {
            // Verifica que a tabela começa vazia
            assertEquals(0, TransacaoDAO.all().count())

            // Executa o seed
            seedTransacao()

            // Verifica que os dados foram inseridos
            val count = TransacaoDAO.all().count()
            println("Transações criadas: $count")
            assertTrue(count >= 290, "Esperado pelo menos 290 transações após o seed")

            val transacao = TransacaoDAO.all().first()
            assertTrue(transacao.valor > 0)
            assertTrue(transacao.categoria.isNotEmpty())
            assertTrue(transacao.descricao.isNotEmpty())
        }
    }
}

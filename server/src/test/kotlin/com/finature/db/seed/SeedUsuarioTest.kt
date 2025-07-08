package com.finature.db.seed

import com.finature.db.tables.*
import com.finature.db.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class SeedUsuarioTest {

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
        }
    }

    @Test
    fun `seedUsuario deve popular tabela UsuarioDAO`() {
        transaction {
            // Verifica que a tabela começa vazia
            assertEquals(0, UsuarioDAO.all().count())

            // Executa o seed
            seedUsuario()

            // Verifica que os dados foram inseridos
            val count = UsuarioDAO.all().count()
            println("Usuários criados: $count")
            assertTrue(count > 0, "Esperado pelo menos 1 usuário após o seed")

            val usuario = UsuarioDAO.all().first()
            assertTrue(usuario.nome.isNotEmpty(), "Nome do usuário não deve estar vazio")
            assertTrue(usuario.email.isNotEmpty(), "Email do usuário não deve estar vazio")
            assertTrue(usuario.senha.isNotEmpty(), "Senha do usuário não deve estar vazia")
        }
    }
}

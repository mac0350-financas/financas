package com.finature.repositories

/* -------- imports de teste -------- */
import kotlin.test.*
import org.junit.jupiter.api.assertThrows

/* -------- imports de banco -------- */
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.exceptions.ExposedSQLException
import com.finature.db.tables.UsuarioTable
import com.finature.db.dao.UsuarioDAO

/* -------- imports de domínio -------- */
import com.finature.models.UsuarioDTO

/**
 * Testes de integração (nível repositório) para [UsuarioRepository].
 *
 * ▸ Usa H2 em memória; cada teste recria as tabelas para isolar estado.  
 * ▸ Não há mocks – validamos a persistência real com Exposed.
 */
class UsuarioRepositoryTest {

    private lateinit var repo: UsuarioRepository

    @BeforeTest
    fun setupDb() {
        // 1 – conecta a banco H2 que vive só na JVM de teste
        Database.connect("jdbc:h2:mem:usuarioRepo;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // 2 – cria as tabelas necessárias
        transaction {
            SchemaUtils.drop(UsuarioTable)
            SchemaUtils.create(UsuarioTable)
        }

        repo = UsuarioRepository()
    }

    /* ---------- CASOS DE TESTE ---------- */

    @Test
    fun `salvaUsuario persiste e pode ser consultado`() {
        val dto = UsuarioDTO(nome = "Alice", email = "alice@exa.com", senha = "123")
        repo.salvaUsuario(dto)

        val salvo = repo.verificaEmailExistente("alice@exa.com")
        assertNotNull(salvo)
        assertEquals("Alice", salvo.nome)
        assertEquals("alice@exa.com", salvo.email)
    }

    @Test
    fun `verificaEmailExistente devolve null quando nao ha registro`() {
        val resultado = repo.verificaEmailExistente("notfound@exa.com")
        assertNull(resultado)
    }

    @Test
    fun `salvaUsuario lança excecao ao inserir email duplicado`() {
        val dto = UsuarioDTO(nome = "Bob", email = "bob@exa.com", senha = "abc")
        repo.salvaUsuario(dto)

        // segundo insert com mesmo email deve violar UNIQUE
        assertThrows<ExposedSQLException> {
            repo.salvaUsuario(dto.copy(nome = "Outro", senha = "xyz"))
        }
    }
}

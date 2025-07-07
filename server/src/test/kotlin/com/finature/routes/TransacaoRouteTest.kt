package com.finature.routes

/* ---- Ktor + Teste ---- */
import io.ktor.server.testing.*
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.test.*
import org.junit.jupiter.api.BeforeEach

/* ---- Banco ---- */
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.finature.db.tables.*

/* ---- Domínio ---- */
import com.finature.sessions.UsuarioSessao
import com.finature.repositories.UsuarioRepository
import com.finature.services.UsuarioService

class TransacaoRouteTest {

    private val emailOk = "alice@example.com"
    private val senhaOk = "123456"

    /** Banco H2 fresh a cada teste */
    @BeforeEach
    fun preparaBanco() {
        val dbName = "test_${System.nanoTime()}"
        Database.connect(
            "jdbc:h2:mem:$dbName;MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(UsuarioTable, TransacaoTable, MetaTable, InvestimentoTable)
            UsuarioService(UsuarioRepository()).criarConta("Alice", emailOk, senhaOk)
        }
    }

    /** Pequena Application DSL só com o que a rota precisa */
    private fun Application.configBasica() {
        install(Sessions) { cookie<UsuarioSessao>("USUARIO_SESSAO") }
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        routing { 
            usuarioRoute()
            transacaoRoute()
        }
    }

    /** Helper para simular login e obter cookie de sessão */
    private suspend fun ApplicationTestBuilder.fazerLogin(): String {
        val resp = client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$emailOk","senha":"$senhaOk"}""")
        }
        assertEquals(HttpStatusCode.OK, resp.status)
        return resp.setCookie().single().value
    }

    /* ---------- TESTES POST /formulario-transacao ---------- */

    @Test
    fun `criar transacao sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-transacao") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "descricao":"Compra supermercado",
                    "valor":150.50,
                    "categoria":"Alimentação",
                    "data":"2024-01-15",
                    "tipoId":-1
                }
            """)
        }
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    /* ---------- TESTES GET /api/transacoes/total ---------- */

    @Test
    fun `buscar total sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/transacoes/total?tipo=-1&mes=01&ano=2024")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    /* ---------- TESTES GET /api/transacoes/lista ---------- */

    @Test
    fun `buscar lista sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/transacoes/lista?tipo=-1&mes=01&ano=2024")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    /* ---------- TESTES GET /api/transacoes/grafico ---------- */

    @Test
    fun `buscar grafico sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/transacoes/grafico?tipo=-1&mes=01&ano=2024")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }


}

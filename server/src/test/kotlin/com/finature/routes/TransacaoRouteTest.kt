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
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.dao.id.EntityID
import com.finature.db.tables.*

/* ---- Domínio ---- */
import com.finature.sessions.UsuarioSessao
import com.finature.repositories.UsuarioRepository
import com.finature.services.UsuarioService

class TransacaoRouteTest {

    private val nomeOk = "Alice"
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
            SchemaUtils.create(UsuarioTable, TransacaoTable, TipoTransacaoTable, MetaTable, InvestimentoTable)
            UsuarioTable.deleteWhere { UsuarioTable.email eq emailOk }
            UsuarioService(UsuarioRepository()).criarConta(nomeOk, emailOk, senhaOk)

            TipoTransacaoTable.insert {
                it[id] = EntityID(-1, TipoTransacaoTable)
                it[tipo] = -1
            }
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
            setBody("""{"nome":"$nomeOk", "email":"$emailOk","senha":"$senhaOk"}""")
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
                    "data":"2024-01-15",
                    "descricao":"Compra supermercado",
                    "categoria":"Alimentação",
                    "valor":150.50,
                    "tipoId":-1,
                    "usuarioId":1
                }
            """)
        }
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    @Test
    fun `criar transacao com autenticacao devolve 201`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.post("/formulario-transacao") {
            contentType(ContentType.Application.Json)
            cookie("USUARIO_SESSAO", cookie)
            setBody("""
                {
                    "descricao":"Compra supermercado",
                    "valor":150.50,
                    "categoria":"Alimentação",
                    "data":"2024-01-15",
                    "tipoId":-1,
                    "usuarioId":1
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, resp.status)
        assertTrue(resp.bodyAsText().contains("Transação realizada com sucesso"))
    }

    /* ---------- TESTES GET /api/transacoes/total ---------- */

    @Test
    fun `buscar total sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/transacoes/total?tipo=-1&mes=01&ano=2024")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    @Test
    fun `buscar total sem parametro tipo devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/total?mes=01&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'tipo' é obrigatório"))
    }

    @Test
    fun `buscar total sem parametro mes devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/total?tipo=-1&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'mes' é obrigatório"))
    }

    @Test
    fun `buscar total sem parametro ano devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/total?tipo=-1&mes=01") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'ano' é obrigatório"))
    }

    @Test
    fun `buscar total com parametros validos devolve 200`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/total?tipo=-1&mes=01&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.OK, resp.status)
        assertTrue(resp.bodyAsText().contains("total"))
    }
    

    /* ---------- TESTES GET /api/transacoes/lista ---------- */

    @Test
    fun `buscar lista sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/transacoes/lista?tipo=-1&mes=01&ano=2024")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    @Test
    fun `buscar lista sem parametro tipo devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/lista?mes=01&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'tipo' é obrigatório"))
    }

    @Test
    fun `buscar lista sem parametro mes devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/lista?tipo=-1&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'mes' é obrigatório"))
    }

    @Test
    fun `buscar lista sem parametro ano devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/lista?tipo=-1&mes=01") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'ano' é obrigatório"))
    }

    @Test
    fun `buscar lista com parametros validos devolve 200`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/lista?tipo=-1&mes=01&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.OK, resp.status)
        assertTrue(resp.bodyAsText().contains("lista"))
    }

    /* ---------- TESTES GET /api/transacoes/grafico ---------- */

    @Test
    fun `buscar grafico sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/transacoes/grafico?tipo=-1&mes=01&ano=2024")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    @Test
    fun `buscar grafico sem parametro tipo devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/grafico?mes=01&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'tipo' é obrigatório"))
    }

    @Test
    fun `buscar grafico sem parametro mes devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/grafico?tipo=-1&ano=2024") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'mes' é obrigatório"))
    }

    @Test
    fun `buscar grafico sem parametro ano devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/transacoes/grafico?tipo=-1&mes=01") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'ano' é obrigatório"))
    }

}

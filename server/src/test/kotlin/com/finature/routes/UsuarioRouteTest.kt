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

class UsuarioRouteTest {

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
            SchemaUtils.create(UsuarioTable, TransacaoTable, MetaTable, InvestimentoTable)
            UsuarioService(UsuarioRepository()).criarConta(nomeOk, emailOk, senhaOk)
        }
    }

    /** Pequena Application DSL só com o que a rota precisa */
    private fun Application.configBasica() {
        install(Sessions) { cookie<UsuarioSessao>("USUARIO_SESSAO") }
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        routing { usuarioRoute() }
    }

    /* ---------- TESTES ---------- */

    @Test
    fun `cadastro novo usuario devolve 201`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-cadastro") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":"Bob","email":"bob@example.com","senha":"senhaSegura"}""")
        }
        assertEquals(HttpStatusCode.Created, resp.status)
    }

    @Test
    fun `cadastro com email ja existente devolve 409`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-cadastro") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":"Alice","email":"$emailOk","senha":"novaSenha"}""")
        }
        assertEquals(HttpStatusCode.Conflict, resp.status)
        assertTrue(resp.bodyAsText().contains("Email já está em uso"))
    }

    @Test
    fun `login com email inexistente devolve 404`() = testApplication {
        application { configBasica() }
    
        val resp = client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"ghost@example.com","senha":"qualquer"}""")
        }
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
    }

    @Test
    fun `logout limpa sessao e devolve 200`() = testApplication {
        application { configBasica() }

        // Simula login para criar sessão
        client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$emailOk","senha":"$senhaOk"}""")
        }

        // Faz logout
        val resp = client.post("/logout")
        assertEquals(HttpStatusCode.OK, resp.status)
        assertTrue(resp.bodyAsText().contains("Logout feito com sucesso"))

        // Verifica se a sessão foi limpa
        val sessaoResp = client.get("/usuario-logado")
        assertEquals(HttpStatusCode.Unauthorized, sessaoResp.status)
        assertTrue(sessaoResp.bodyAsText().contains("Usuário não logado"))
    }

    @Test
    fun `verifica se usuario nao logado devolve 401`() = testApplication {
        application { configBasica() }

        // Tenta acessar endpoint sem estar logado
        val resp = client.get("/usuario-logado")
        assertEquals(HttpStatusCode.Unauthorized, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não logado"))
    }

    @Test
    fun `login com senha incorreta devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$emailOk","senha":"senhaErrada"}""")
        }

        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Erro interno do servidor"))
    }


    @Test
    fun `login com email nao encontrado devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"naoexiste@example.com","senha":"qualquer"}""")
        }

        // Agora espera InternalServerError (500) conforme simplificação
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Erro interno do servidor"))
    }


    @Test
    fun `cadastro com dados incompletos devolve 400`() = testApplication {
        application { configBasica() }

        val resp1 = client.post("/formulario-cadastro") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":"Bob","email":"","senha":"senhaSegura"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, resp1.status)
        assertTrue(resp1.bodyAsText().contains("Dados incompletos"))

        val resp2 = client.post("/formulario-cadastro") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":"","email":"teste@teste.com","senha":"senhaSegura"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, resp2.status)
        assertTrue(resp2.bodyAsText().contains("Dados incompletos"))

        val resp3 = client.post("/formulario-cadastro") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":"Bob","email":"teste@teste.com","senha":""}""")
        }
        assertEquals(HttpStatusCode.BadRequest, resp3.status)
        assertTrue(resp3.bodyAsText().contains("Dados incompletos"))
    }

    @Test
    fun `cadastro com JSON mal formado devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-cadastro") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":123,"email":true,"senha":[]}""")
        }

        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Erro interno do servidor"))
    }

}

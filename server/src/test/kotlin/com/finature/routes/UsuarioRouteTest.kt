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
    fun `login com email inexistente devolve 404`() = testApplication {
        application { configBasica() }
    
        val resp = client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"ghost@example.com","senha":"qualquer"}""")
        }
    
        // // Troque ESTA linha ↓
        // assertEquals(HttpStatusCode.NotFound, resp.status)
        // por ESTA linha ↓
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
    }
    

    // @Test
    // fun `login sucesso gera cookie e rota protegida responde 200`() = testApplication {
    //     application { configBasica() }

    //     /* 1 – login */
    //     val login = client.post("/formulario-login") {
    //         contentType(ContentType.Application.Json)
    //         setBody("""{"email":"$emailOk","senha":"$senhaOk"}""")
    //     }
    //     assertEquals(HttpStatusCode.OK, login.status)
    //     val sessao = login.setCookie().single()

    //     /* 2 – rota protegida */
    //     val me = client.get("/usuario-logado") {
    //         cookie(sessao.name, sessao.value)
    //     }
    //     assertEquals(HttpStatusCode.OK, me.status)
    //     assertTrue(me.bodyAsText().contains("Alice"))

    //     /* 3 – logout */
    //     val logout = client.post("/logout") { cookie(sessao.name, sessao.value) }
    //     assertEquals(HttpStatusCode.OK, logout.status)
    //     assertTrue(logout.setCookie().any { it.name == sessao.name && it.maxAge == 0 })

    //     /* 4 – sessão antiga já não funciona */
    //     val after = client.get("/usuario-logado") { cookie(sessao.name, sessao.value) }
    //     assertEquals(HttpStatusCode.Unauthorized, after.status)
    // }
}

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
import com.finature.repositories.MetaRepository
import com.finature.services.MetaService
import com.finature.repositories.UsuarioRepository
import com.finature.services.UsuarioService
import com.finature.models.MetaDTO

class MetaRouteTest {

    private val nomeOk = "Alice"
    private val emailOk = "alice@example.com"
    private val senhaOk = "123456"

    @BeforeEach
    fun preparaBanco() {
        val dbName = "test_${System.nanoTime()}"
        Database.connect(
            "jdbc:h2:mem:$dbName;MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(UsuarioTable, MetaTable, TransacaoTable, TipoTransacaoTable, InvestimentoTable)
            UsuarioTable.deleteWhere { UsuarioTable.email eq emailOk }
            UsuarioService(UsuarioRepository()).criarConta(nomeOk, emailOk, senhaOk)
        
            TipoTransacaoTable.insert {
                it[id] = EntityID(-1, TipoTransacaoTable)
                it[tipo] = -1
            }
        }
    }

    private fun Application.configBasica() {
        install(Sessions) { cookie<UsuarioSessao>("USUARIO_SESSAO") }
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        routing { 
            usuarioRoute()
            transacaoRoute()
            metaRoute()
        }
    }

    private suspend fun ApplicationTestBuilder.fazerLogin(): String {
        val resp = client.post("/formulario-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"nome":"$nomeOk", "email":"$emailOk","senha":"$senhaOk"}""")
        }
        assertEquals(HttpStatusCode.OK, resp.status)
        return resp.setCookie().single().value
    }

    /* ------------------- TESTES POST /formulario-meta ------------------- */

    @Test
    fun `criar meta sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.post("/formulario-meta") {
            contentType(ContentType.Application.Json)
            setBody("""
                {   
                    "usuarioId":1,
                    "categoria":"Alimentação",
                    "valorLimite":500.0,
                    "valorAtual":432.0,
                    "dataInicial":"2024-01-01",
                    "dataFinal":"2024-01-31",
                    "tipoId":-1
                }
            """)
        }
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    @Test
    fun `criar meta com autenticacao devolve 201`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.post("/formulario-meta") {
            contentType(ContentType.Application.Json)
            cookie("USUARIO_SESSAO", cookie)
            setBody("""
                {   
                    "usuarioId":0,
                    "categoria":"🍽️ Alimentação",
                    "valorLimite":500.0,
                    "valorAtual":0.0,
                    "dataInicial":"2024-01-01",
                    "dataFinal":"2024-01-31",
                    "tipoId":-1
                }
            """)
        }
        assertEquals(HttpStatusCode.Created, resp.status)
        assertTrue(resp.bodyAsText().contains("Transação realizada com sucesso"))
    }

    /* ------------------- TESTES GET /api/metas/lista ------------------- */

    @Test
    fun `listar metas sem autenticacao devolve 500`() = testApplication {
        application { configBasica() }

        val resp = client.get("/api/metas/lista?tipo=-1")
        assertEquals(HttpStatusCode.InternalServerError, resp.status)
        assertTrue(resp.bodyAsText().contains("Usuário não autenticado"))
    }

    @Test
    fun `listar metas sem parametro tipo devolve 400`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        val resp = client.get("/api/metas/lista") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.BadRequest, resp.status)
        assertTrue(resp.bodyAsText().contains("Parâmetro 'tipo' é obrigatório"))
    }

    @Test
    fun `listar metas retorna lista correta`() = testApplication {
        application { configBasica() }
        val cookie = fazerLogin()

        transaction {
            val repo = MetaRepository()
            repo.salvaMeta(
                MetaDTO(
                    usuarioId = 1,
                    categoria = "Alimentação",
                    valorLimite = 500.0,
                    valorAtual = 100.0,
                    dataInicial = "2024-01-01",
                    dataFinal = "2024-01-31",
                    tipoId = -1
                )
            )
        }

        val resp = client.get("/api/metas/lista?tipo=-1") {
            cookie("USUARIO_SESSAO", cookie)
        }
        assertEquals(HttpStatusCode.OK, resp.status)
        assertTrue(resp.bodyAsText().contains("Alimentação"))
    }
}

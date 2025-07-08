package com.finature.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString 

import com.finature.models.MetaDTO
import com.finature.services.MetaService
import com.finature.repositories.MetaRepository 
import com.finature.sessions.UsuarioSessao
import com.finature.services.TransacaoService
import com.finature.repositories.TransacaoRepository

fun Route.metaRoute() {

    post("/formulario-meta") {

        try {
            val request = call.receive<MetaDTO>()
            val usuarioSessao = call.sessions.get<UsuarioSessao>()
                ?: throw IllegalStateException("Usuário não autenticado")

            val metaRepository = MetaRepository()
            val metaService = MetaService(metaRepository)
            val transacaoRepository = TransacaoRepository()
            val transacaoService = TransacaoService(transacaoRepository)

            val valorAtual = transacaoService.somaTransacoesMeta(
                usuarioId = usuarioSessao.id ?: throw IllegalStateException("ID do usuário não encontrado"),
                tipoId = request.tipoId,
                categoria = request.categoria,
                dataInicial = request.dataInicial,
                dataFinal = request.dataFinal
            )

            metaService.criarMeta(
                usuarioId = usuarioSessao.id,
                categoria = request.categoria,
                valorLimite = request.valorLimite,
                valorAtual = valorAtual,
                dataInicial = request.dataInicial,
                dataFinal = request.dataFinal,
                tipoId = request.tipoId
            )
            call.respond(HttpStatusCode.Created, mapOf("message" to "Transação realizada com sucesso"))
        }

        catch (e: Exception) {
            val statusCode = HttpStatusCode.InternalServerError
            val message = e.message ?: "Erro interno do servidor"
            call.respond(statusCode, mapOf("message" to message))
        }

    }

    route("/api/metas") {

        get("/lista") {

            try {
                val usuarioSessao = call.sessions.get<UsuarioSessao>()
                    ?: throw IllegalStateException("Usuário não autenticado")
                val tipo = call.request.queryParameters["tipo"]
                    ?: throw IllegalArgumentException("Parâmetro 'tipo' é obrigatório")

                val metaRepository = MetaRepository()
                val metaService = MetaService(metaRepository)
                val transacaoRepository = TransacaoRepository()
                val transacaoService = TransacaoService(transacaoRepository)
                
                val lista = metaService.listaMetas(
                    usuarioId = usuarioSessao.id ?: throw IllegalStateException("ID do usuário não encontrado"),
                    tipo = tipo.toInt()
                )

                lista.forEach { meta ->
                    val novoValorAtual = transacaoService.somaTransacoesMeta(
                        usuarioId = meta.usuarioId,
                        tipoId = meta.tipoId,
                        categoria = meta.categoria,
                        dataInicial = meta.dataInicial,
                        dataFinal = meta.dataFinal
                    )
                    meta.valorAtual = novoValorAtual
                }

                call.respond(HttpStatusCode.OK, mapOf("lista" to lista))
            }

            catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.message))
            }

            catch (e: Exception) {
                val statusCode = HttpStatusCode.InternalServerError
                val message = e.message ?: "Erro interno do servidor"
                call.respond(statusCode, mapOf("message" to message))
            }

        }

    }


}
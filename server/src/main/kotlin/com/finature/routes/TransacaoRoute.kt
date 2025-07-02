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

import com.finature.models.TransacaoDTO
import com.finature.services.TransacaoService
import com.finature.repositories.TransacaoRepository 
import com.finature.sessions.UsuarioSessao

fun Route.transacaoRoute() {

    post("/formulario-transacao") {

        try {
            val request = call.receive<TransacaoDTO>()
            val usuarioSessao = call.sessions.get<UsuarioSessao>()
                ?: throw IllegalStateException("Usuário não autenticado")
            val transacaoRepository = TransacaoRepository()
            val transacaoService = TransacaoService(transacaoRepository)
            transacaoService.criarTransacao(
                usuarioId = usuarioSessao.id,
                descricao = request.descricao,
                valor = request.valor,
                categoria = request.categoria,
                data = request.data,
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

}
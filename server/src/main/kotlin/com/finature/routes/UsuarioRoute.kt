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

import com.finature.models.UsuarioDTO
import com.finature.services.UsuarioService
import com.finature.repositories.UsuarioRepository 
import com.finature.sessions.UsuarioSessao

fun Route.usuarioRoute() {
    
    post("/formulario-cadastro") {

        try {
            val request = call.receive<UsuarioDTO>()
            val usuarioRepository = UsuarioRepository()
            val usuarioService = UsuarioService(usuarioRepository)
            usuarioService.criarConta(
                nome = request.nome,
                email = request.email,
                senha = request.senha
            )
            call.respond(HttpStatusCode.Created, mapOf("message" to "Conta criada com sucesso"))
        } 
        
        catch (e: Exception) {
            val (statusCode, message) = when (e.message) {
                "Email já está em uso" -> HttpStatusCode.Conflict to "Email já está em uso"
                else -> HttpStatusCode.InternalServerError to "Erro interno do servidor"
            }
            call.respond(statusCode, mapOf("message" to message))
        }

    }

    post("/formulario-login") {

        try {
            val request = call.receive<UsuarioDTO>()
            val usuarioRepository = UsuarioRepository()
            val usuarioService = UsuarioService(usuarioRepository)
            val usuario = usuarioService.fazerLogin(
                email = request.email,
                senha = request.senha
            )
            call.sessions.set(UsuarioSessao(usuario.nome, usuario.email))
            call.respond(HttpStatusCode.OK, mapOf("message" to "Login realizado com sucesso"))
        } 
        
        catch (e: Exception) {
            val (statusCode, message) = when (e.message) {
                "Email não encontrado" -> HttpStatusCode.NotFound to "Email não encontrado"
                "Senha incorreta" -> HttpStatusCode.Unauthorized to "Senha incorreta"
                else -> HttpStatusCode.InternalServerError to "Erro interno do servidor"
            }
            call.respond(statusCode, mapOf("message" to message))
        }

    }

    post("/logout") {
        call.sessions.clear<UsuarioSessao>()
        call.respond(HttpStatusCode.OK, mapOf("message" to "Logout feito com sucesso"))
    }

    get("/usuario-logado") {
        val sessao = call.sessions.get<UsuarioSessao>()
        if (sessao == null) {
            call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Usuário não logado"))
        } else {
            call.respond(HttpStatusCode.OK, sessao)
        }
    }
    
    
}

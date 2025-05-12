package com.finature.routes

import com.finature.models.Transacao
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val transacoesSaida = mutableListOf<Transacao>()

fun Route.transacaoSaidaRoutes() {

    route("/transacao-saida") {

        post {
            val transacao = call.receive<Transacao>()
            transacoesSaida.add(transacao)
            println("Transação de saída recebida: $transacao")
            call.respondText("Transação de saída registrada com sucesso!")
        }

        get {
            call.respond(transacoesSaida)
        }
    }
}
  
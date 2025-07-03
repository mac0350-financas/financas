package com.finature

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.slf4j.event.*
import com.finature.routes.usuarioRoute

import com.finature.routes.transacaoRoute
import com.finature.routes.investimentoRoute

fun Application.configureRouting() {
    
    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        usuarioRoute()
        transacaoRoute()
        investimentoRoute()
    }
}

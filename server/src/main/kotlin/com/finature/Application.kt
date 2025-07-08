package com.finature
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.server.netty.EngineMain
import com.finature.sessions.UsuarioSessao

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Sessions) {
        cookie<UsuarioSessao>("usuario_sessao") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = null
        }
    }

    configureHTTP()
    configureDatabases()
    seedDatabases()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}

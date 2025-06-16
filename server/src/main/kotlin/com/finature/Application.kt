package com.finature
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureDatabases()
    seedDatabases()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}

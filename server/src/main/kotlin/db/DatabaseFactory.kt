package com.finature

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    
    Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // memória temporária
        driver = "org.h2.Driver",
        user = "root",
        password = ""
    )

    // Inicializa as tabelas
    /*transaction {
        SchemaUtils.create(
        )
    }*/
}

 
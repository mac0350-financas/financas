package com.finature

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import io.ktor.server.application.Application

fun Application.configureDatabases() {

    val DB_URL = "jdbc:mariadb://localhost:3306/finature"
    val DB_USER = "finature"
    val DB_PASSWORD = "finature"
    
    Database.connect(
        url = DB_URL,
        user = DB_USER,
        password = DB_PASSWORD,
    )

    Flyway.configure()
          .dataSource(DB_URL, DB_USER, DB_PASSWORD)
          .locations("classpath:migration")
          .load()
          .migrate()

}


 
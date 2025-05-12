package com.finature

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import com.finature.db.tables.*
import org.jetbrains.exposed.sql.SchemaUtils
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    
    val DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val DB_USER = "root"
    val DB_PASSWORD = ""

    Database.connect(
        url = DB_URL,
        driver = "org.h2.Driver",
        user = DB_USER,
        password = DB_PASSWORD,
    )

    Flyway.configure()
          .dataSource(DB_URL, DB_USER, DB_PASSWORD)
          .locations("classpath:db/migration")
          .load()
          .migrate()

    transaction {
        SchemaUtils.create(
            UsuarioTable,
            InvestimentoTable,
            MetaTable,
            TransacaoTable,
            TipoTransacaoTable,
        )
    }
}

 
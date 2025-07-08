package com.finature.db.seed

import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.sql.*

fun seedTipoTransacao() {
    if (TipoTransacaoTable.select { TipoTransacaoTable.id eq 1 }.empty()) {
        TipoTransacaoTable.insert {
            it[id] = 1
            it[tipo] = 1
        }
    }

    if (TipoTransacaoTable.select { TipoTransacaoTable.id eq -1 }.empty()) {
        TipoTransacaoTable.insert {
            it[id] = -1
            it[tipo] = -1
        }
    }
}

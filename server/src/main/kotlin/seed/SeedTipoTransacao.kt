package com.finature.seed

import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.sql.insertIgnore

fun seedTipoTransacao() {

        TipoTransacaoTable.insertIgnore {
            it[id] = 1
            it[tipo] = 1
        }

        TipoTransacaoTable.insertIgnore {
            it[id] = -1
            it[tipo] = -1
        }
    
}

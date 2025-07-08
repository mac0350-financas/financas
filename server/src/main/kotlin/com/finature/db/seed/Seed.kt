package com.finature

import org.jetbrains.exposed.sql.transactions.transaction
import com.finature.db.seed.seedUsuario
import com.finature.db.seed.seedTipoTransacao
import com.finature.db.seed.seedTransacao
import com.finature.db.seed.seedInvestimento
import com.finature.db.seed.seedMeta

fun seedDatabases() {
    transaction {
        seedUsuario()
        seedTipoTransacao()
        seedTransacao()
        seedInvestimento()
        seedMeta()
    }
}
 
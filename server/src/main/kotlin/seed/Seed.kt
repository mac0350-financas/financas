package com.finature

import org.jetbrains.exposed.sql.transactions.transaction
import com.finature.seed.seedUsuario
import com.finature.seed.seedTipoTransacao
import com.finature.seed.seedTransacao
import com.finature.seed.seedInvestimento
import com.finature.seed.seedMeta

fun seedDatabases() {
    transaction {
        seedUsuario()
        seedTipoTransacao()
        seedTransacao()
        seedInvestimento()
        seedMeta()
    }
}
 
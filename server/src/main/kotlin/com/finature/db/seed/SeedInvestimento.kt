package com.finature.db.seed

import com.finature.db.dao.InvestimentoDAO
import com.finature.db.dao.UsuarioDAO
import com.github.javafaker.Faker

fun seedInvestimento() {
    val faker = Faker()
    val usuarios = UsuarioDAO.all().toList()

    if (InvestimentoDAO.all().empty()) {

        repeat(10) {
            val usuario = usuarios.random()

            InvestimentoDAO.new {
                this.usuarioId = usuario.id
                this.valorInvestido = faker.number().randomDouble(2, 100, 2000)
                this.taxa = faker.number().randomDouble(2, 100, 2000)
                this.dataInicial = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).toInstant().toString()
                this.dataFinal = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).toInstant().toString()
            }
        }
    }
}
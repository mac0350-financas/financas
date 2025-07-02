package com.finature.seed

import com.finature.db.dao.TransacaoDAO
import com.finature.db.dao.TipoTransacaoDAO
import com.finature.db.dao.UsuarioDAO
import com.github.javafaker.Faker

fun seedTransacao() {
    val faker = Faker()
    val usuarios = UsuarioDAO.all().toList()
    val tiposTransacao = TipoTransacaoDAO.all().toList()

    repeat(100) {
        val usuario = usuarios.random()
        val tipoTransacao = tiposTransacao.random()

        TransacaoDAO.new {
            this.data = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).toInstant().toString()
            this.valor = faker.number().randomDouble(2, 10, 1000)            
            this.tipoId = tipoTransacao.id
            this.categoria = faker.lorem().sentence(3)
            this.descricao = faker.lorem().sentence(3)
            this.usuarioId = usuario.id        }
    }
}
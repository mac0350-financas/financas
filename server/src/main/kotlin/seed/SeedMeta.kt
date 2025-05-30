package com.finature.seed

import com.finature.db.dao.MetaDAO
import com.finature.db.dao.UsuarioDAO
import com.github.javafaker.Faker

fun seedMeta() {
    val faker = Faker()
    val usuarios = UsuarioDAO.all().toList()

    repeat(10) {
        val usuario = usuarios.random()

        MetaDAO.new {
            usuarioId = usuario.id
            categoria = faker.commerce().department()
            valorLimite = faker.number().randomDouble(2, 100, 2000)
            periodo = listOf(30.0, 90.0, 180.0, 365.0).random()
        }

    }

}
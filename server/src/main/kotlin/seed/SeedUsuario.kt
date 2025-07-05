package com.finature.seed

import com.finature.db.dao.UsuarioDAO
import com.github.javafaker.Faker
import org.mindrot.jbcrypt.BCrypt

fun seedUsuario() {
    val faker = Faker()

    if (UsuarioDAO.all().empty()) {
        repeat(3) {
            val nome = faker.name().fullName()
            val email = faker.internet().emailAddress()
            val senhaHash = BCrypt.hashpw("123456", BCrypt.gensalt())

            UsuarioDAO.new {
                this.nome = nome
                this.email = email
                this.senha = senhaHash
            }
        }

        UsuarioDAO.new {
            this.nome = "finature"
            this.email = "finature@finature.com"
            this.senha = "finature"
        }

    }

}

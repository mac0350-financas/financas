package com.finature.db.dao

import com.finature.db.tables.UsuarioTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class UsuarioDAOTest {

    @BeforeTest
    fun setup() {
        Database.connect(
            "jdbc:h2:mem:test_${System.nanoTime()};MODE=MariaDB;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(UsuarioTable)
        }
    }

    @Test
    fun `inserir, consultar, atualizar e deletar UsuarioDAO`() {
        transaction {
            // Insere um usuário
            val usuario = UsuarioDAO.new {
                nome = "Teste Usuario"
                email = "teste@usuario.com"
                senha = "senha123"
            }

            // Consulta por id
            val consulta = UsuarioDAO.findById(usuario.id)
            assertNotNull(consulta)
            assertEquals("Teste Usuario", consulta?.nome)
            assertEquals("teste@usuario.com", consulta?.email)

            // Testa conversão para DTO
            val dto = consulta?.toDTO()
            assertNotNull(dto)
            assertEquals(usuario.id.value, dto?.id) // Id confere
            assertEquals("Teste Usuario", dto?.nome)
            assertEquals("teste@usuario.com", dto?.email)

            // Atualiza o nome do usuário
            consulta?.nome = "Usuario Atualizado"
            assertEquals("Usuario Atualizado", consulta?.nome)

            // Deleta o registro
            consulta?.delete()
            assertNull(UsuarioDAO.findById(usuario.id))
        }
    }
}
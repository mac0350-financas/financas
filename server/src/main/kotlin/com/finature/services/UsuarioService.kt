package service

import model.Usuario

class UsuarioService {
    private val usuarios = mutableListOf<Usuario>()

    fun cadastrar(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun autenticar(email: String, senha: String): Usuario? {
        return usuarios.find { it.email == email && it.senha == senha }
    }

    fun listar(): List<Usuario> = usuarios
}
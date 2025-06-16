package com.finature.services

import com.finature.repositories.UsuarioRepository
import com.finature.models.UsuarioDTO

class UsuarioService(private val usuarioRepository: UsuarioRepository) {
    
    fun criarConta(nome: String, email: String, senha: String) {

        val usuarioExiste = usuarioRepository.verificaEmailExistente(email)
        if (usuarioExiste != null) throw Exception("Email já está em uso")
        
        val usuario = UsuarioDTO(
            nome = nome,
            email = email,
            senha = senha
        )
        
        usuarioRepository.salvaUsuario(usuario)
    }
}
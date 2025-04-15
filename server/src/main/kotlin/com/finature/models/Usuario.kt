package com.finature.models


// class (e não data class) porque as variáveis de instância
// são mutáveis 
class Usuario(

    var id: Int,
    var nome: String,
    var email: String,
    var senha: String,

) {

    fun atualizarNome(novoNome: String) {
        this.nome = novoNome
    }

    fun atualizarEmail(novoEmail: String) {
        this.email = novoEmail
    }

    fun atualizarSenha(novaSenha: String) {
        this.senha = novaSenha
    }

    fun autenticar(email: String, senha: String): Boolean {
        return this.email == email && this.senha == senha
    }

}

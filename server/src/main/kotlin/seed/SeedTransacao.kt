package com.finature.seed

import com.finature.db.dao.TransacaoDAO
import com.finature.db.dao.TipoTransacaoDAO
import com.finature.db.dao.UsuarioDAO
import com.github.javafaker.Faker

fun seedTransacao() {
    val faker = Faker()
    val usuarios = UsuarioDAO.all().toList()
    val tiposTransacao = TipoTransacaoDAO.all().toList()
    
    val categorias = listOf(
        "🍽️ Alimentação", "🚗 Transporte", "🩺 Saúde", "🎓 Educação", "🎉 Lazer", "🏠 Moradia",
        "👚 Vestuário", "💼 Negócios", "💸 Dívidas", "📈 Investimentos", "💝 Doação e presente",
        "🐶 Pets", "✨ Outros"
    )

    repeat(100) {
        val usuario = usuarios.random()
        val tipoTransacao = tiposTransacao.random()
        val dataAleatoria = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS)
        val dataFormatada = String.format("%02d/%02d/%04d", 
            dataAleatoria.date, 
            dataAleatoria.month + 1, 
            dataAleatoria.year + 1900)

        TransacaoDAO.new {
            this.data = dataFormatada
            this.valor = faker.number().randomDouble(2, 10, 1000)            
            this.tipoId = tipoTransacao.id
            this.categoria = categorias.random()
            this.descricao = faker.lorem().sentence(3)
            this.usuarioId = usuario.id        
        }
    }
}
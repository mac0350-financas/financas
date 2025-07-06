package com.finature.seed

import com.finature.db.dao.TransacaoDAO
import com.finature.db.dao.TipoTransacaoDAO
import com.finature.db.dao.UsuarioDAO
import com.github.javafaker.Faker

fun seedTransacao() {
    val faker = Faker()
    val usuarios = UsuarioDAO.all().toList()
    val tiposTransacao = TipoTransacaoDAO.all().toList()
    
    val categoriasG = listOf(
        "🍽️ Alimentação", "🚗 Transporte", "🩺 Saúde", "🎓 Educação", "🎉 Lazer", "🏠 Moradia",
        "👚 Vestuário", "💼 Negócios", "💸 Dívidas", "📈 Investimentos", "💝 Doação e presente",
        "🐶 Pets", "✨ Outros"
    )

    val categoriasR = listOf(
        "💼 Salário", "💰 Freelance", "📈 Investimentos", "🎁 Presente", "💸 Reembolso", 
        "🏠 Aluguel", "🛒 Venda", "🤝 Parceria", "🎥 Streaming", "✨ Outros"
    )

    repeat(300) {
        val usuario = usuarios.random()
        val tipoTransacao = tiposTransacao.random()
        val dataAleatoria = faker.date().past(1000, java.util.concurrent.TimeUnit.DAYS)
        val dataFormatada = String.format("%04d-%02d-%02d", 
            dataAleatoria.year + 1900,
            dataAleatoria.month + 1, 
            dataAleatoria.date
        )  

        TransacaoDAO.new {
            this.data = dataFormatada
            this.valor = faker.number().randomDouble(2, 10, 1000)            
            this.tipoId = tipoTransacao.id
            this.categoria = if (tipoTransacao.id.value == -1) categoriasG.random() else categoriasR.random()
            this.descricao = faker.lorem().sentence(3)
            this.usuarioId = usuario.id        
        }
    }
}
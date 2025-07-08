package com.finature.db.seed

import com.finature.db.dao.MetaDAO
import com.finature.db.dao.UsuarioDAO
import com.finature.db.dao.TipoTransacaoDAO
import com.github.javafaker.Faker

fun seedMeta() {
    val faker = Faker()
    val usuarios = UsuarioDAO.all().toList()
    val tiposTransacao = TipoTransacaoDAO.all().toList()

    if (MetaDAO.all().empty()) {

        val categoriasG = listOf(
            "🍽️ Alimentação", "🚗 Transporte", "🩺 Saúde", "🎓 Educação", "🎉 Lazer", "🏠 Moradia",
            "👚 Vestuário", "💼 Negócios", "💸 Dívidas", "📈 Investimentos", "💝 Doação e presente",
            "🐶 Pets", "🛒 Mercado", "✈️ Viagem", "✨ Outros"
        )

        val categoriasR = listOf(
            "💼 Salário", "💰 Freelance", "📈 Investimentos", "🎁 Presente", "💸 Reembolso", 
            "🏠 Aluguel", "🛒 Venda", "🤝 Parceria", "🎥 Streaming", "✨ Outros"
        )


        repeat(10) {
            val usuario = usuarios.random()
            val tipoTransacao = tiposTransacao.random()
            val dataAleatoriaInicial = faker.date().past(1000, java.util.concurrent.TimeUnit.DAYS)
            val dataFormatadaInicial = String.format("%04d-%02d-%02d", 
                dataAleatoriaInicial.year + 1900,
                dataAleatoriaInicial.month + 1, 
                dataAleatoriaInicial.date
            )
            val dataAleatoriaFinal = faker.date().future(1000, java.util.concurrent.TimeUnit.DAYS)
            val dataFormatadaFinal = String.format("%04d-%02d-%02d", 
                dataAleatoriaFinal.year + 1900,
                dataAleatoriaFinal.month + 1, 
                dataAleatoriaFinal.date
            )  

            MetaDAO.new {
                usuarioId = usuario.id
                this.tipoId = tipoTransacao.id
                this.categoria = if (tipoTransacao.id.value == -1) categoriasG.random() else categoriasR.random()
                valorLimite = faker.number().randomDouble(2, 100, 2000)
                valorAtual = faker.number().randomDouble(2, 100, 2000) 
                dataInicial = dataFormatadaInicial
                dataFinal = dataFormatadaFinal 
            }

        }
    }

}
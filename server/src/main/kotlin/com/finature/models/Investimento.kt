package com.finature.models
import java.time.LocalDate

data class Investimento(

    val id: Int,
    val usuarioId: Int,
    val valorInvestido: Double,
    val taxa: Double,
    val dataInicial: LocalDate,
    val dataFinal: LocalDate,

) {

    fun calculaRendimento(): Double {
        val diasInvestidos = dataFinal.toEpochDay() - dataInicial.toEpochDay()
        val rendimento = valorInvestido * taxa * diasInvestidos / 365
        return rendimento
    }

}
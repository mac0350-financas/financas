package com.finature.service

import com.finature.models.SimulationRequest
import com.finature.models.SimulationPoint
import com.finature.models.Investmento
import kotlin.math.max
import kotlin.math.pow

object InvestCalcService {
    
    fun anualParaMensal(taxaAnual: Double): Double = 
        (1 + taxaAnual).pow(1.0 / 12.0) - 1

    fun mensalParaAnual(taxaMensal: Double): Double = 
        (1 + taxaMensal).pow(12.0) - 1
    
    /** Calcula a alíquota de IR baseada no tempo de aplicação */
    private fun calcularAliquotaIR(meses: Int): Double = when {
        meses <= 6 -> 0.225   // 22,5%
        meses <= 12 -> 0.20   // 20%
        meses <= 24 -> 0.175  // 17,5%
        else -> 0.15          // 15%
    }
    
    /** Calcula a taxa anual da poupança considerando a SELIC.
     *  Se a SELIC > 8,5%, a taxa é de 0,5% ao mês + TR(taxa referencial mensal).
     *  Caso contrário, a taxa é 70% da SELIC anual.
     */
    fun calcularPoupancaAnual(selicAnual: Double, tr: Double): Double =
        if (selicAnual > 0.085) mensalParaAnual(tr + 0.5/100) else selicAnual * 0.70
    
    fun simularInvestimento(
        request: SimulationRequest,
        taxaAnual: Double,
        limiteMeses: Int = 600
    ): List<SimulationPoint> {
        val taxaMensal = anualParaMensal(taxaAnual)
        val historico = mutableListOf<SimulationPoint>()
        var saldo = request.aporteInicial
        val limiteEfetivo = minOf(request.tempoMeses, limiteMeses)
        
        for (mes in 0..limiteEfetivo) {
            if (mes != 0) {
                saldo = saldo * (1 + taxaMensal) + request.aporteMensal
            }
            historico.add(SimulationPoint(mes, (saldo * 100).toInt() / 100.0))
        }
        return historico
    }
    
    fun calcularDetalhes(
        request: SimulationRequest,
        serie: List<SimulationPoint>,
        isPoupanca: Boolean = false
    ): Investmento {
        val valorBruto = serie.last().valor
        val totalInvestido = request.aporteInicial + (request.aporteMensal * request.tempoMeses)
        val ganhoBruto = valorBruto - totalInvestido
        val rentabilidadeBruta = ganhoBruto / totalInvestido
        
        // Poupança é isenta de IR
        val valorIR = if (isPoupanca) 0.0 else {
            val aliquota = calcularAliquotaIR(request.tempoMeses)
            max(0.0, ganhoBruto * aliquota)
        }
        
        val valorLiquido = valorBruto - valorIR
        val ganhoLiquido = valorLiquido - totalInvestido
        val rentabilidadeLiquida = ganhoLiquido / totalInvestido
        
        val resultado = Investmento(
            valorBruto = valorBruto,
            rentabilidadeBruta = rentabilidadeBruta,
            valorIR = valorIR,
            valorLiquido = valorLiquido,
            rentabilidadeLiquida = rentabilidadeLiquida,
            ganhoLiquido = ganhoLiquido,
            totalInvestido = totalInvestido
        )
        
        println("Detalhes calculados (isPoupanca=$isPoupanca): $resultado")
        return resultado
    }
}

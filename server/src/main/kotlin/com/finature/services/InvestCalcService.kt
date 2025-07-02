package com.finature.service

import com.finature.models.SimulationRequest
import com.finature.models.SimulationPoint
import kotlin.math.pow

object InvestCalcService {
    
    fun anualParaMensal(taxaAnual: Double): Double = 
        (1 + taxaAnual).pow(1.0 / 12.0) - 1

    fun mensalParaAnual(taxaMensal: Double): Double = 
        (1 + taxaMensal).pow(12.0) - 1
    
    /** Calcula a taxa anual da poupança considerando a SELIC.
     *  Se a SELIC > 8,5%, a taxa é de 0,5% ao mês + TR(taxa referncial mensal).
     *  Caso contrário, a taxa é 70% da SELIC anual.
     */
    fun calcularPoupancaAnual(selicAnual: Double, tr: Double): Double =
        if (selicAnual > 0.085) mensalParaAnual(tr+(0.5)/100) else selicAnual*0.70
    
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
}

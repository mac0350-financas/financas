package com.finature.services

import com.finature.services.InvestimentoService
import com.finature.models.SimulationRequest
import com.finature.models.SimulationPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class InvestimentoServiceTest {

    @Test
    fun `anualParaMensal deve converter corretamente`() {
        val taxaAnual = 0.12
        val taxaMensal = InvestimentoService.anualParaMensal(taxaAnual)
        //o último valor abaixo na função abaixo é um delta de tolerância 
        //para comparação de ponto flutuante
        assertEquals(0.0095, taxaMensal, 0.0001)
    }

    @Test
    fun `anualParaMensal com taxa zero`() {
        val taxaAnual = 0.0
        val taxaMensal = InvestimentoService.anualParaMensal(taxaAnual)
        assertEquals(0.0, taxaMensal, 0.0001)
    }

    @Test
    fun `mensalParaAnual deve converter corretamente`() {
        val taxaMensal = 0.01 // 1% a.m.
        val taxaAnual = InvestimentoService.mensalParaAnual(taxaMensal)
        assertEquals(0.1268, taxaAnual, 0.0001) // ~12.68% a.a.
    }

    @Test
    fun `mensalParaAnual com taxa zero`() {
        val taxaMensal = 0.0
        val taxaAnual = InvestimentoService.mensalParaAnual(taxaMensal)
        assertEquals(0.0, taxaAnual, 0.0001)
    }

    @Test
    fun `calcularPoupancaAnual com SELIC alta`() {
        val selicAnual = 0.10 // 10% - acima de 8.5%, poupanca = 0.5% a.m. + TR
        val tr = 0.001 // 0.1% a.m.
        val taxaPoupanca = InvestimentoService.calcularPoupancaAnual(selicAnual, tr)
        // 0.5% + 0.1% = 0.6% a.m. Convertido para anual = 7.44%
        assertEquals(0.0744, taxaPoupanca, 0.0001)
    }

    @Test
    fun `calcularPoupancaAnual com SELIC baixa`() {
        val selicAnual = 0.08 // 8% - abaixo de 8.5%
        val tr = 0.001
        val taxaPoupanca = InvestimentoService.calcularPoupancaAnual(selicAnual, tr)
        assertEquals(0.0560, taxaPoupanca, 0.0001) // 70% de 8%
    }

    @Test
    fun `calcularPoupancaAnual com SELIC exatamente 8,5pct`() {
        val selicAnual = 0.085
        val tr = 0.001
        val taxaPoupanca = InvestimentoService.calcularPoupancaAnual(selicAnual, tr)
        assertEquals(0.0595, taxaPoupanca, 0.0001) // 70% de 8.5%
    }

    @Test
    fun `simularInvestimento cenário básico`() {
        val request = SimulationRequest(
            aporteInicial = 1000.0,
            aporteMensal = 100.0,
            tempoMeses = 3
        )
        val taxaAnual = 0.12 // 12% a.a.
        
        val resultado = InvestimentoService.simularInvestimento(request, taxaAnual)
        
        assertEquals(4, resultado.size) // mês 0, 1, 2, 3
        assertEquals(1000.0, resultado[0].valor, 0.01) // mês 0
        assertTrue(resultado[1].valor > 1100.0) // mês 1 com rendimento
        assertTrue(resultado[2].valor > resultado[1].valor) // crescimento
        assertTrue(resultado[3].valor > resultado[2].valor) // crescimento
    }

    @Test
    fun `simularInvestimento sem aporte inicial`() {
        val request = SimulationRequest(
            aporteInicial = 0.0,
            aporteMensal = 100.0,
            tempoMeses = 2
        )
        val taxaAnual = 0.12
        
        val resultado = InvestimentoService.simularInvestimento(request, taxaAnual)
        
        assertEquals(3, resultado.size)
        assertEquals(0.0, resultado[0].valor)
        assertEquals(100.0, resultado[1].valor)
        assertTrue(resultado[2].valor > 200.0)
    }

    @Test
    fun `calcularDetalhes para investimento Selic`() {
        val request = SimulationRequest(
            aporteInicial = 1000.0,
            aporteMensal = 100.0,
            tempoMeses = 12
        )
        val serie = listOf(
            SimulationPoint(0, 1000.0),
            SimulationPoint(12, 2428.89)
        )
        
        val detalhes = InvestimentoService.calcularDetalhes(request, serie, false)
        
        //ganho liquido = 2428.89 - 2200.0 (1000 + 12*100)
        assertEquals(2428.89, detalhes.valorBruto)
        assertEquals(2200.0, detalhes.totalInvestido) // 1000 + 12*100
        assertEquals(45.78, detalhes.valorIR, 0.01) // IR calculado (0.2* 228.89)
        assertEquals(2383.11, detalhes.valorLiquido, 0.01) // total bruto - IR
        assertEquals(228.89, detalhes.ganhoLiquido + detalhes.valorIR, 0.01) // ganho bruto
        assertTrue(detalhes.valorIR > 0) // deve ter IR
        assertTrue(detalhes.valorLiquido < detalhes.valorBruto) // líquido menor que bruto
    }

    @Test
    fun `calcularDetalhes para poupança sem IR`() {
        val request = SimulationRequest(
            aporteInicial = 1000.0,
            aporteMensal = 100.0,
            tempoMeses = 12
        )
        val serie = listOf(
            SimulationPoint(0, 1000.0),
            SimulationPoint(12, 2500.0)
        )
        
        val detalhes = InvestimentoService.calcularDetalhes(request, serie, true)
        
        assertEquals(0.0, detalhes.valorIR) // poupança é isenta
        assertEquals(detalhes.valorBruto, detalhes.valorLiquido)
    }

    @Test
    fun `calcularDetalhes diferentes períodos de IR`() {
        val request6meses = SimulationRequest(1000.0, 0.0, 6)
        val request12meses = SimulationRequest(1000.0, 0.0, 12)
        val request24meses = SimulationRequest(1000.0, 0.0, 24)
        val request25meses = SimulationRequest(1000.0, 0.0, 25)
        
        //queremos simular o mesmo valor com diferentes meses para ver o efeito das aliquotas
        val serie = listOf(
            SimulationPoint(6, 2000.0),
            SimulationPoint(12, 2000.0),
            SimulationPoint(24, 2000.0),
            SimulationPoint(25, 2000.0)
        )
        
        val detalhes6m = InvestimentoService.calcularDetalhes(request6meses, serie, false)
        val detalhes12m = InvestimentoService.calcularDetalhes(request12meses, serie, false)
        val detalhes24m = InvestimentoService.calcularDetalhes(request24meses, serie, false)
        val detalhes25m = InvestimentoService.calcularDetalhes(request25meses, serie, false)
        
        // IR deve diminuir com o tempo (22.5%, 20%, 17.5%, 15%)
        assertTrue(detalhes6m.valorIR > detalhes12m.valorIR)
        assertTrue(detalhes12m.valorIR > detalhes24m.valorIR)
        assertTrue(detalhes24m.valorIR > detalhes25m.valorIR)
    }
}

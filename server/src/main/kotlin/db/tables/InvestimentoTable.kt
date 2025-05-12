package com.finature.db.tables

import org.jetbrains.exposed.sql.Table
import com.finature.db.tables.UsuarioTable 
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.dao.id.IntIdTable

object InvestimentoTable : IntIdTable("investimentos") {
    val usuarioId = reference("usuario_id", UsuarioTable)
    val valorInvestido = double("valor_investido")
    val taxa = double("taxa")
    val dataInicial = date("data_inicial")
    val dataFinal = date("data_final")
}
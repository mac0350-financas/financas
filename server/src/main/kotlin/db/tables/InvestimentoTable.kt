package com.finature.db.tables

import org.jetbrains.exposed.sql.Table
import com.finature.db.tables.UsuarioTable 
import org.jetbrains.exposed.sql.javatime.date

object InvestimentoTable : Table("investimentos") {
    val id = integer("id").autoIncrement() 
    val usuarioId = reference("usuario_id", UsuarioTable.id)
    val valorInvestido = double("valor_investido")
    val taxa = double("taxa")
    val dataInicial = date("data_inicial")
    val dataFinal = date("data_final")

    override val primaryKey = PrimaryKey(id)
}
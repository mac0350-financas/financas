package com.finature.db.tables
import org.jetbrains.exposed.sql.Table
import com.finature.db.tables.TipoTransacaoTable
import org.jetbrains.exposed.dao.id.IntIdTable

object MetaTable : IntIdTable("metas") {
    val usuarioId = reference("usuario_id", UsuarioTable)
    val categoria = varchar("categoria", 255)
    val valorLimite = double("valor_limite")
    val valorAtual = double("valor_atual")
    val dataInicial = varchar("data_inicial", 255)
    val dataFinal = varchar("data_final", 255)
    val tipoId = reference("tipo", TipoTransacaoTable)
} 

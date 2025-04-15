package com.finature.models

data class TipoTransacao(val sinal: Int) {
    ENTRADA(1),
    SAIDA(-1);

    fun aplica(valor: Double): Double {
        return valor * sinal
    }
}
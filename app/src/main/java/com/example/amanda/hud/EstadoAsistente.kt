package com.example.amanda.hud

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class EstadoAsistente {
    INACTIVO,
    ESCUCHANDO,
    PROCESANDO,
    HABLANDO
}

class EstadoAsistenteHolder {
    private val _estado = MutableStateFlow(EstadoAsistente.INACTIVO)
    val estado: StateFlow<EstadoAsistente> = _estado.asStateFlow()

    fun cambiarA(nuevo: EstadoAsistente) {
        _estado.value = nuevo
    }
}

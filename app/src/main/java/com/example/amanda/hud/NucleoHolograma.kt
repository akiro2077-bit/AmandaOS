package com.example.amanda.hud

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

enum class EstadoAsistente {
    INACTIVO,
    ESCUCHANDO,
    PROCESANDO,
    HABLANDO
}

@Composable
fun NucleoHolograma(
    modifier: Modifier = Modifier,
    estado: EstadoAsistente = EstadoAsistente.INACTIVO
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hologram_transition")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val color = when (estado) {
        EstadoAsistente.INACTIVO -> Color.Gray
        EstadoAsistente.ESCUCHANDO -> Color.Cyan
        EstadoAsistente.PROCESANDO -> Color.Magenta
        EstadoAsistente.HABLANDO -> Color.Green
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 3

        drawCircle(
            color = color,
            center = center,
            radius = radius,
            style = Stroke(width = 8f)
        )
    }
}

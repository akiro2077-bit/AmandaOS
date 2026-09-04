package com.example.amanda.hud

import android.animation.ValueAnimator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

private val NucleoColor = Color(0xFFF4ECFF)
private val AnilloVioleta = Color(0xFF9B5CFF)
private val AnilloMagenta = Color(0xFFD84BFF)
private val AcentoIndigo = Color(0xFF7C6CE0)

private data class Particula(
    val anguloBase: Float,
    val radioFactor: Float,
    val velocidad: Float,
    val tamanoDp: Float
)

@Composable
fun NucleoHolograma(
    estado: EstadoAsistente,
    modifier: Modifier = Modifier
) {
    val animacionesActivas = remember { ValueAnimator.areAnimatorsEnabled() }

    val particulas = remember {
        List(90) {
            Particula(
                anguloBase = Random.nextFloat() * 360f,
                radioFactor = 0.35f + Random.nextFloat() * 0.55f,
                velocidad = 0.4f + Random.nextFloat() * 0.8f,
                tamanoDp = 1f + Random.nextFloat() * 2f
            )
        }
    }

    val duracionRotacionMs = when (estado) {
        EstadoAsistente.INACTIVO -> 20000
        EstadoAsistente.ESCUCHANDO -> 4000
        EstadoAsistente.PROCESANDO -> 1800
        EstadoAsistente.HABLANDO -> 6000
    }

    val transicion = rememberInfiniteTransition(label = "nucleo")
    val anguloAnimado by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duracionRotacionMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angulo"
    )
    val angulo = if (animacionesActivas) anguloAnimado else 0f

    val compresion by animateFloatAsState(
        targetValue = if (estado == EstadoAsistente.PROCESANDO) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "compresion"
    )

    val brillo by animateFloatAsState(
        targetValue = when (estado) {
            EstadoAsistente.INACTIVO -> 0.45f
            EstadoAsistente.ESCUCHANDO -> 1f
            EstadoAsistente.PROCESANDO -> 0.8f
            EstadoAsistente.HABLANDO -> 1f
        },
        animationSpec = tween(durationMillis = 400),
        label = "brillo"
    )

    val direccionAnillo2 = if (estado == EstadoAsistente.PROCESANDO) -1.6f else 1.3f

    Canvas(modifier = modifier.size(260.dp)) {
        val radioMax = min(size.width, size.height) / 2f
        val centro = Offset(size.width / 2f, size.height / 2f)

        fun cajaAnillo(radio: Float): Pair<Offset, Size> = Pair(
            Offset(centro.x - radio, centro.y - radio),
            Size(radio * 2f, radio * 2f)
        )

        rotate(degrees = angulo, pivot = centro) {
            for (i in 0 until 16) {
                val rad = Math.toRadians((i * 22.5f).toDouble())
                val r1 = radioMax * 0.28f
                val r2 = radioMax * (0.7f - compresion * 0.15f)
                drawLine(
                    color = AnilloVioleta.copy(alpha = 0.18f * brillo),
                    start = Offset(centro.x + cos(rad).toFloat() * r1, centro.y + sin(rad).toFloat() * r1),
                    end = Offset(centro.x + cos(rad).toFloat() * r2, centro.y + sin(rad).toFloat() * r2),
                    strokeWidth = 1.dp.toPx(),
                    blendMode = BlendMode.Plus
                )
            }
        }

        rotate(degrees = angulo, pivot = centro) {
            val radio = radioMax * (0.5f - compresion * 0.08f)
            val (topLeft, tam) = cajaAnillo(radio)
            drawArc(
                color = AnilloVioleta.copy(alpha = 0.55f * brillo),
                startAngle = 0f,
                sweepAngle = 290f,
                useCenter = false,
                topLeft = topLeft,
                size = tam,
                style = Stroke(width = 2.5.dp.toPx()),
                blendMode = BlendMode.Plus
            )
        }

        rotate(degrees = angulo * direccionAnillo2, pivot = centro) {
            val radio = radioMax * (0.72f - compresion * 0.12f)
            val (topLeft, tam) = cajaAnillo(radio)
            drawArc(
                color = AnilloMagenta.copy(alpha = 0.4f * brillo),
                startAngle = 0f,
                sweepAngle = 250f,
                useCenter = false,
                topLeft = topLeft,
                size = tam,
                style = Stroke(width = 1.5.dp.toPx()),
                blendMode = BlendMode.Plus
            )
        }

        particulas.forEach { p ->
            val rad = Math.toRadians((p.anguloBase + angulo * p.velocidad).toDouble())
            val radio = radioMax * p.radioFactor * (1f - compresion * 0.35f)
            val pos = Offset(centro.x + cos(rad).toFloat() * radio, centro.y + sin(rad).toFloat() * radio)
            drawCircle(
                color = AcentoIndigo.copy(alpha = 0.55f * brillo),
                radius = p.tamanoDp.dp.toPx() / 2f,
                center = pos,
                blendMode = BlendMode.Plus
            )
        }

        drawCircle(
            color = NucleoColor.copy(alpha = brillo),
            radius = radioMax * (0.16f + compresion * 0.04f),
            center = centro,
            blendMode = BlendMode.Plus
        )
    }
}

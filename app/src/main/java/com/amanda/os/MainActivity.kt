package com.amanda.os

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

enum class AssistantState { IDLE, LISTENING, PROCESSING, SPEAKING }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmandaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF030206)
                ) {
                    AmandaHomeScreen()
                }
            }
        }
    }
}

@Composable
fun AmandaTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
fun AmandaHomeScreen() {
    var state by remember { mutableStateOf(AssistantState.IDLE) }
    var statusText by remember { mutableStateOf("Sistemas en línea. En espera, Maestro.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Encabezado
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "AMANDA.OS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF5E8FF),
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = "NÚCLEO HOLOGRÁFICO MORADO",
                fontSize = 11.sp,
                color = Color(0xFFA855F7),
                letterSpacing = 2.sp
            )
        }

        // Núcleo Holográfico
        Box(
            modifier = Modifier
                .size(320.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PurpleHologramCore(state = state)
        }

        // Estado y Controles
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = statusText,
                color = Color(0xFFE9D5FF),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    state = when (state) {
                        AssistantState.IDLE -> {
                            statusText = "Escuchando a Maestro..."
                            AssistantState.LISTENING
                        }
                        AssistantState.LISTENING -> {
                            statusText = "Procesando..."
                            AssistantState.PROCESSING
                        }
                        AssistantState.PROCESSING -> {
                            statusText = "Amanda.os respondiendo..."
                            AssistantState.SPEAKING
                        }
                        AssistantState.SPEAKING -> {
                            statusText = "En espera, Maestro."
                            AssistantState.IDLE
                        }
                    }
                },
                modifier = Modifier
                    .size(80.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7))
            ) {
                Text("MIC", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PurpleHologramCore(state: AssistantState) {
    val infiniteTransition = rememberInfiniteTransition(label = "coreAnim")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state == AssistantState.PROCESSING) 1500 else 6000,
                easing = LinearEasing
            )
        ),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.width / 2

        // 1. Resplandor Cian Fantasma desenfocado
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x2606B6D4), Color.Transparent),
                center = center,
                radius = maxRadius
            ),
            center = center,
            radius = maxRadius
        )

        // 2. Anillos Concéntricos en rotación
        rotate(rotation, center) {
            drawOval(
                color = Color(0xFFA855F7),
                topLeft = Offset(center.x - maxRadius * 0.7f, center.y - maxRadius * 0.25f),
                size = androidx.compose.ui.geometry.Size(maxRadius * 1.4f, maxRadius * 0.5f),
                style = Stroke(width = 3f)
            )
        }

        rotate(-rotation * 1.5f, center) {
            drawOval(
                color = Color(0xFFEC4899),
                topLeft = Offset(center.x - maxRadius * 0.5f, center.y - maxRadius * 0.7f),
                size = androidx.compose.ui.geometry.Size(maxRadius * 1.0f, maxRadius * 1.4f),
                style = Stroke(width = 2f)
            )
        }

        // 3. Esfera Central Incandescente Morada
        val currentPulse = if (state == AssistantState.SPEAKING) pulse else 1f
        val coreRadius = (maxRadius * 0.35f) * currentPulse

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFF5E8FF),
                    Color(0xFFA855F7),
                    Color(0xFFEC4899),
                    Color.Transparent
                ),
                center = center,
                radius = coreRadius * 1.5f
            ),
            center = center,
            radius = coreRadius * 1.5f,
            blendMode = BlendMode.Screen
        )
    }
}

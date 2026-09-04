package com.example.amanda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.amanda.hud.EstadoAsistenteHolder
import com.example.amanda.hud.NucleoHolograma

class MainActivity : ComponentActivity() {
    private val estadoHolder = EstadoAsistenteHolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val estado by estadoHolder.estado.collectAsState()
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0D0B18)),
                contentAlignment = Alignment.Center
            ) {
                NucleoHolograma(estado = estado)
            }
        }
    }
}

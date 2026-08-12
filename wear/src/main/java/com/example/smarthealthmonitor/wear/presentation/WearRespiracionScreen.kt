package com.example.smarthealthmonitor.wear.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import kotlinx.coroutines.delay

enum class FaseRespiracion(val etiqueta: String, val duracionSegundos: Int, val escalaObjetivo: Float) {
    INHALAR("Inhala...", 4, 1.0f),
    SOSTENER("Sostén el aire", 4, 1.0f),
    EXHALAR("Exhala suave...", 4, 0.45f)
}

enum class EstadoSesion {
    INICIO,
    EN_CURSO,
    FINALIZADO
}

@Composable
fun WearRespiracionScreen(
    fcActual: Int,
    onRegistrarResultado: (Int) -> Unit = {},
    onVolver: () -> Unit = {}
) {
    var estadoSesion by remember { mutableStateOf(EstadoSesion.INICIO) }
    var faseActual by remember { mutableStateOf(FaseRespiracion.INHALAR) }
    var segundosRestantesFase by remember { mutableIntStateOf(4) }
    var cicloActual by remember { mutableIntStateOf(1) }
    val maxCiclos = 4

    var fcInicial by remember { mutableIntStateOf(fcActual) }
    var fcFinal by remember { mutableIntStateOf(fcActual) }

    // Animación fluida de escala para el círculo guía
    val escalaAnimada by animateFloatAsState(
        targetValue = if (estadoSesion == EstadoSesion.EN_CURSO) faseActual.escalaObjetivo else 0.7f,
        animationSpec = tween(
            durationMillis = if (faseActual == FaseRespiracion.SOSTENER) 300 else 4000,
            easing = FastOutSlowInEasing
        ),
        label = "escalaCirculoRespiracion"
    )

    // Color temático relajante según la fase
    val colorFase by animateColorAsState(
        targetValue = when (faseActual) {
            FaseRespiracion.INHALAR -> Color(0xFF00E5FF) // Cian brillante
            FaseRespiracion.SOSTENER -> Color(0xFF76FF03) // Verde lima calma
            FaseRespiracion.EXHALAR -> Color(0xFF2979FF) // Azul profundo
        },
        animationSpec = tween(durationMillis = 1000),
        label = "colorFaseRespiracion"
    )

    // Temporizador principal de la sesión
    LaunchedEffect(estadoSesion, faseActual) {
        if (estadoSesion == EstadoSesion.EN_CURSO) {
            segundosRestantesFase = faseActual.duracionSegundos
            while (segundosRestantesFase > 0) {
                delay(1000L)
                segundosRestantesFase--
            }

            // Transición entre fases
            when (faseActual) {
                FaseRespiracion.INHALAR -> faseActual = FaseRespiracion.SOSTENER
                FaseRespiracion.SOSTENER -> faseActual = FaseRespiracion.EXHALAR
                FaseRespiracion.EXHALAR -> {
                    if (cicloActual < maxCiclos) {
                        cicloActual++
                        faseActual = FaseRespiracion.INHALAR
                    } else {
                        // Sesión terminada con éxito
                        fcFinal = fcActual
                        estadoSesion = EstadoSesion.FINALIZADO
                        onRegistrarResultado(fcFinal)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B132B)),
        contentAlignment = Alignment.Center
    ) {
        when (estadoSesion) {
            EstadoSesion.INICIO -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🌬️ Respiración",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E5FF)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "FC actual: $fcActual bpm",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )

                    Text(
                        text = "4 ciclos (1 min de calma)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            fcInicial = if (fcActual > 0) fcActual else 75
                            cicloActual = 1
                            faseActual = FaseRespiracion.INHALAR
                            estadoSesion = EstadoSesion.EN_CURSO
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF))
                    ) {
                        Text("Comenzar", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            EstadoSesion.EN_CURSO -> {
                // Círculo animado de respiración
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(escalaAnimada)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(colorFase.copy(alpha = 0.45f), colorFase.copy(alpha = 0.05f))
                            )
                        )
                        .border(2.dp, colorFase.copy(alpha = 0.8f), CircleShape)
                )

                // Información superpuesta en el centro
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ciclo $cicloActual/$maxCiclos",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = faseActual.etiqueta,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "${segundosRestantesFase}s",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorFase
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "❤️ $fcActual bpm",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            EstadoSesion.FINALIZADO -> {
                val diferencia = fcInicial - fcFinal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "✨ ¡Sesión Lista!",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF76FF03)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Inicial: $fcInicial bpm → Final: $fcFinal bpm",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 11.sp
                    )

                    Text(
                        text = if (diferencia > 0) "📉 -$diferencia bpm (Relajado)" else "Calma alcanzada",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onVolver,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                    ) {
                        Text("Finalizar", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

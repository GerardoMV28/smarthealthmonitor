package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text

@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B4A))) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            TvLazyColumn(
                modifier = Modifier.fillMaxSize().padding(40.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Cabecera con botón de actualizar
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SmartHealth Monitor — TV (Neon Serverless)",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            onClick = { viewModel.refresh() },
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = Color(0xFF1565C0),
                                focusedContainerColor = Color(0xFF42A5F5)
                            ),
                            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("↺ Actualizar", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }

                // Fila 1: Estado / Promedios por Dispositivo
                item {
                    RowSection(title = "📊 Estado Actual (3 Dispositivos)") {
                        if (state.estadisticas.isEmpty() && state.lecturas.isEmpty()) {
                            Text("No hay datos disponibles", color = Color.Gray)
                        } else {
                            val itemsToShow = if (state.estadisticas.isNotEmpty()) state.estadisticas else state.lecturas.take(3)
                            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(itemsToShow) { lectura ->
                                    FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
                                }
                            }
                        }
                    }
                }

                // Fila 2: Historial Completo
                item {
                    RowSection(title = "📋 Historial Completo (Últimas 50 lecturas)") {
                        if (state.lecturas.isEmpty()) {
                            Text("No hay lecturas registradas en Neon", color = Color.Gray)
                        } else {
                            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(state.lecturas) { lectura ->
                                    FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
                                }
                            }
                        }
                    }
                }

                // Fila 3: Alertas / Consultas avanzadas
                if (state.alertas.isNotEmpty()) {
                    item {
                        RowSection(title = "⚠️ Alertas Recientes (FC fuera de rango)") {
                            TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(state.alertas) { lectura ->
                                    FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

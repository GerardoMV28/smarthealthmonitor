package com.example.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.mediarouter.app.MediaRouteButton
import com.example.smarthealthmonitor.SmartHealthMonitorTheme
import com.example.smarthealthmonitor.TarjetaDato
import com.example.smarthealthmonitor.FilaHistorial
import com.example.smarthealthmonitor.viewmodel.DashboardViewModel
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {

    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()

    // Estado del diálogo
    var mostrarAlerta by remember { mutableStateOf(false) }

    // Estado del Snackbar
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Mostrar diálogo
    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = { nota ->
                mostrarAlerta = false
                scope.launch {
                    val resultado = snackbarHost.showSnackbar(
                        message = "✅ Alerta enviada a tus contactos de emergencia",
                        actionLabel = "Deshacer",
                        duration = SnackbarDuration.Long
                    )
                    if (resultado == SnackbarResult.ActionPerformed) {
                        snackbarHost.showSnackbar(
                            message = "⚠️ Alerta cancelada",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SmartHealth",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    // Botón de Cast (Chromecast) integrado vía AndroidView
                    AndroidView(
                        factory = { context ->
                            MediaRouteButton(context).apply {
                                CastButtonFactory.setUpMediaRouteButton(context, this)
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarAlerta = true },
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Enviar alerta de emergencia",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TarjetaDato(
                    valor = "$fc",
                    unidad = "bpm",
                    label = "Frecuencia cardíaca",
                    colorValor = MaterialTheme.colorScheme.error
                )
            }

            item {
                TarjetaDato(
                    valor = "%,d".format(pasos),
                    unidad = "pasos",
                    label = "Pasos del día",
                    colorValor = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historial reciente",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = onHistorialClick) {
                        Text("Ver todo")
                    }
                }
            }

            items(
                items = historial,
                key = { it.id }
            ) { lectura ->
                FilaHistorial(lectura = lectura)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}

package com.example.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.example.smarthealthmonitor.wear.presentation.components.WearFilaHistorial

@Composable
fun WearHistorialScreen(
    onBack: () -> Unit = {},
    viewModel: WearDashboardViewModel = viewModel()
) {

    val historial by viewModel.historial.collectAsState()

    val listState = rememberScalingLazyListState()

    ScreenScaffold(
        scrollState = listState
    ) {

        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Text("Historial (${historial.size})")
            }

            if (historial.isEmpty()) {

                item {
                    Text("Sin lecturas aún")
                }

            } else {

                items(historial) { bpm ->

                    WearFilaHistorial(
                        bpm = bpm
                    )
                }
            }
        }
    }
}
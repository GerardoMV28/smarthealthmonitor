package com.example.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import com.example.smarthealthmonitor.wear.presentation.components.WearFCCard

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit = {},
    onHistorialClick: () -> Unit = {}
) {

    val viewModel = WearDashboardViewModel()

    val fc by viewModel.fc.collectAsState()

    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            WearFCCard(
                fc = fc,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = onAlertClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Alerta")
            }
        }
        item {
            Button(
                onClick = onHistorialClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Historial")
            }
        }
    }
}
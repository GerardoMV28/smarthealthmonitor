package com.example.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text

@Composable
fun WearFilaHistorial(
    bpm: Int
) {

    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("$bpm bpm")
    }
}
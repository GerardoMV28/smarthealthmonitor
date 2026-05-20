package com.example.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun WearFCCard(
    fc: Int,
    modifier: Modifier = Modifier
) {
    val colorFC = if (fc in 60..100) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Red
    }

    Card(
        onClick = {},
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "❤",
                fontSize = 20.sp
            )

            Text(
                text = "$fc",
                style = MaterialTheme.typography.displayMedium,
                color = colorFC
            )

            Text(
                text = "bpm",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
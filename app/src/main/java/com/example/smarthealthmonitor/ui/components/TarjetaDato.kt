package com.example.smarthealthmonitor
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smarthealthmonitor.SmartHealthMonitorTheme
@Composable
fun TarjetaDato(
    valor: String,
    unidad: String,
    label: String,
    colorValor: Color,
    modifier: Modifier = Modifier
) {

    // Verifica si es frecuencia cardíaca
    val esFrecuenciaCardiaca = unidad == "bpm"

    // Convertir valor a número
    val valorNumerico = valor.toIntOrNull() ?: 0

    // Rango normal
    val esNormal = valorNumerico in 60..100

    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = valor,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorValor
                )

                Text(
                    text = unidad,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorValor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Chip de estado SOLO para frecuencia cardíaca
            if (esFrecuenciaCardiaca) {

                Spacer(Modifier.height(12.dp))

                AssistChip(
                    onClick = {},

                    label = {
                        Text(
                            if (esNormal)
                                "Normal"
                            else
                                "Consulta al médico"
                        )
                    },

                    colors = AssistChipDefaults.assistChipColors(
                        containerColor =
                            if (esNormal)
                                Color(0xFF4CAF50)
                            else
                                MaterialTheme.colorScheme.error,

                        labelColor = Color.White
                    )
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true, name = "TarjetaDato - FC")
@Composable
private fun TarjetaDatoPreview() {

    SmartHealthMonitorTheme {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TarjetaDato(
                valor = "78",
                unidad = "bpm",
                label = "Frecuencia cardíaca",
                colorValor = MaterialTheme.colorScheme.error
            )

            TarjetaDato(
                valor = "4,250",
                unidad = "pasos",
                label = "Pasos del día",
                colorValor = MaterialTheme.colorScheme.primary
            )

            // Ejemplo fuera de rango
            TarjetaDato(
                valor = "120",
                unidad = "bpm",
                label = "Frecuencia cardíaca",
                colorValor = MaterialTheme.colorScheme.error
            )
        }
    }
}
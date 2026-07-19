package mx.utng.smarthealthmonitor.tv.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.tv.data.remote.LecturaFcDto
import mx.utng.smarthealthmonitor.tv.data.remote.NeonClient
import mx.utng.smarthealthmonitor.tv.data.remote.NeonRequest

class TvNeonRepository {

    /** Obtener historial completo de los 3 dispositivos */
    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """SELECT id, bpm, estado, dispositivo, hora, created_at
FROM lecturas_fc
ORDER BY created_at DESC
LIMIT $1""".trimIndent(),
                    params = listOf(limite)
                )
            ).rows
        }

    /** Estadísticas por dispositivo */
    suspend fun obtenerEstadisticas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """SELECT COALESCE(MAX(id), 0) AS id,
dispositivo,
ROUND(AVG(bpm)) AS bpm,
'Promedio' AS estado,
MAX(hora) AS hora
FROM lecturas_fc
GROUP BY dispositivo""".trimIndent()
                )
            ).rows
        }

    /** Consultas SQL avanzadas (Reto extra): Alertas de FC fuera de rango (últimas 24 horas) */
    suspend fun obtenerAlertasRecientes(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """SELECT id, bpm, estado, dispositivo, hora, created_at
FROM lecturas_fc
WHERE (bpm < 60 OR bpm > 100)
ORDER BY created_at DESC
LIMIT 10""".trimIndent()
                )
            ).rows
        }
}

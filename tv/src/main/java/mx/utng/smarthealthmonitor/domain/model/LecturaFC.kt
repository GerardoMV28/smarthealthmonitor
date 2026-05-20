package mx.utng.smarthealthmonitor.domain.model

data class LecturaFC(
    val id: Int = 0,
    val bpm: Int,
    val estado: String,
    val hora: String
)

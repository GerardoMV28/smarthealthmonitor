package mx.utng.smarthealthmonitor.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bpm: Int = 0,
    val estado: String = "Normal",
    val dispositivo: String = "app", // wear | app | tv
    val hora: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
    @ColumnInfo(name = "sincronizado")
    val sincronizado: Boolean = false // false = pendiente de sync
) {
    // Propiedades de compatibilidad con UI existente
    val valorBpm: Int get() = bpm
    val esNormal: Boolean get() = estado == "Normal" || bpm in 60..100
    val timestamp: Long get() = System.currentTimeMillis()
}

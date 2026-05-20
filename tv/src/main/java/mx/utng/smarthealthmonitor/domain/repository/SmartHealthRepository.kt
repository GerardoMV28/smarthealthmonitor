package mx.utng.smarthealthmonitor.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import mx.utng.smarthealthmonitor.domain.model.LecturaFC

interface SmartHealthRepository {
    fun obtenerHistorial(): Flow<List<LecturaFC>>
    val fcActual: StateFlow<Int>
}

class SmartHealthRepositoryImpl : SmartHealthRepository {
    override fun obtenerHistorial(): Flow<List<LecturaFC>> = flowOf(
        listOf(
            LecturaFC(1, 75, "Normal", "10:00 AM"),
            LecturaFC(2, 82, "Normal", "10:30 AM"),
            LecturaFC(3, 95, "Elevado", "11:00 AM")
        )
    )

    override val fcActual: StateFlow<Int> = MutableStateFlow(78)
}

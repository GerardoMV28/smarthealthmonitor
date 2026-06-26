package com.example.smarthealthmonitor.data.models

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mx.utng.smarthealthmonitor.data.local.LecturaFC
import mx.utng.smarthealthmonitor.data.local.SmartHealthDatabase
import mx.utng.smarthealthmonitor.data.repository.SyncRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SmartHealthRepository {

    // FC actual
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()
    val mutableFcFlow: MutableStateFlow<Int> get() = _fcFlow

    // Pasos actuales
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // SyncRepository (Room + Neon)
    private var syncRepository: SyncRepository? = null

    fun init(context: Context) {
        val db = SmartHealthDatabase.getInstance(context)
        syncRepository = SyncRepository(db.lecturaFcDao())
    }

    suspend fun actualizarFC(bpm: Int, dispositivo: String = "wear") {
        _fcFlow.value = bpm

        val estado = when {
            bpm < 60 -> "FC Baja"
            bpm > 100 -> "FC Alta"
            else -> "Normal"
        }
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        // Guardar en Room primero y luego sincronizar hacia Neon
        syncRepository?.insertarLectura(
            LecturaFC(
                bpm = bpm,
                estado = estado,
                dispositivo = dispositivo,
                hora = hora,
                sincronizado = false
            )
        )
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> {
        return syncRepository?.observarHistorial() ?: emptyFlow()
    }

    /** Sincronización manual: enviar pendientes y descargar desde Neon */
    suspend fun sincronizarManual() {
        syncRepository?.enviarPendientes()
        syncRepository?.sincronizarDesdeNeon(limite = 50)
    }
}
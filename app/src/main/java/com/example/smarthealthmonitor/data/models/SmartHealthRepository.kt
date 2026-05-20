package com.example.smarthealthmonitor.data.models

import android.content.Context
import com.example.smarthealthmonitor.data.db.LecturaFC
import com.example.smarthealthmonitor.data.db.LecturaFCDao
import com.example.smarthealthmonitor.data.db.SmartHealthDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow

object SmartHealthRepository {

    // FC actual
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos actuales
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // DAO Room
    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.Companion
            .getDatabase(context)
            .lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {

        _fcFlow.value = bpm

        // Guardar en Room
        dao?.insertar(
            LecturaFC(
                valorBpm = bpm
            )
        )
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun obtenerHistorial():
            Flow<List<LecturaFC>> {

        return dao?.obtenerUltimas()
            ?: emptyFlow()
    }
}
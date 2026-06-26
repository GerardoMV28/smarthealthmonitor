package com.example.smarthealthmonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.local.LecturaFC
import com.example.smarthealthmonitor.data.models.SmartHealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> =
        SmartHealthRepository.fcFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val pasos: StateFlow<Int> =
        SmartHealthRepository.pasosFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun sincronizarManual() {
        viewModelScope.launch {
            SmartHealthRepository.sincronizarManual()
        }
    }
}
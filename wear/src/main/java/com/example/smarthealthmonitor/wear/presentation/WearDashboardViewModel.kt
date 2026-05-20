package com.example.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class WearDashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> =
        flowOf(72)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                72
            )

    // Historial temporal para S10
    val historial: StateFlow<List<Int>> =
        flowOf(
            listOf(
                72,
                84,
                110,
                95,
                78
            )
        ).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}
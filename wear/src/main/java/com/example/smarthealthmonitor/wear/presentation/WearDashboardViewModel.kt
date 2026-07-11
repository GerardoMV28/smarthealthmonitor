package com.example.smarthealthmonitor.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.wear.mqtt.MqttWearPublisher

class WearDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    // Historial temporal
    private val _historial = MutableStateFlow(listOf(72, 84, 110, 95, 78))
    val historial: StateFlow<List<Int>> = _historial.asStateFlow()

    private val mqttPublisher = MqttWearPublisher(application)
    private val neonRepo = mx.utng.smarthealthmonitor.wear.data.WearNeonRepository()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mqttPublisher.connect()
        }
        viewModelScope.launch {
            _fc.collect { bpm ->
                val estado = when {
                    bpm < 60 -> "FC Baja"
                    bpm > 100 -> "FC Alta"
                    else -> "Normal"
                }
                mqttPublisher.publishFC(bpm, estado)

                // Publicar a Neon en IO thread
                launch(Dispatchers.IO) {
                    runCatching { neonRepo.publicarLectura(bpm, estado) }
                        .onFailure { android.util.Log.w("WEAR", "Sin red: ${it.message}") }
                }
            }
        }
    }


    fun actualizarFC(nuevoBpm: Int) {
        _fc.value = nuevoBpm
        _historial.value = listOf(nuevoBpm) + _historial.value.take(9)
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }
}
package mx.utng.smarthealthmonitor.tv.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.tv.data.TvNeonRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

class TvViewModel(private val context: Context? = null) : ViewModel() {

    private val neonRepo = TvNeonRepository()
    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val stats = neonRepo.obtenerEstadisticas()
                val alertas = neonRepo.obtenerAlertasRecientes()
                val ultimaFc = lecturas.firstOrNull()?.bpm ?: 0

                _state.update {
                    it.copy(
                        lecturas = lecturas.map { dto -> dto.toLecturaFC() },
                        estadisticas = stats.map { dto -> dto.toLecturaFC() },
                        alertas = alertas.map { dto -> dto.toLecturaFC() },
                        fcActual = ultimaFc,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun refresh() = cargarDatos()
}

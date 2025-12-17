package com.lifelog.feature.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.usecase.AddMoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogUiState(
    val mood: Int = 3, // 1-5
    val energy: Float = 0.5f, // 0.0 - 1.0
    val stress: Float = 0.5f, // 0.0 - 1.0
    val libido: Float = 0.5f, // 0.0 - 1.0
    val notes: String = ""
)

@HiltViewModel
class LogViewModel @Inject constructor(
    private val addMoodUseCase: AddMoodUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    fun onMoodChange(mood: Int) {
        _uiState.value = _uiState.value.copy(mood = mood)
    }

    fun onEnergyChange(energy: Float) {
        _uiState.value = _uiState.value.copy(energy = energy)
    }

    fun onStressChange(stress: Float) {
        _uiState.value = _uiState.value.copy(stress = stress)
    }

    fun onLibidoChange(libido: Float) {
        _uiState.value = _uiState.value.copy(libido = libido)
    }

    fun onNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun saveEntry() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val mood = Mood(
                timestamp = System.currentTimeMillis(),
                rating = currentState.mood,
                energy = (currentState.energy * 10).toInt(),
                stress = (currentState.stress * 10).toInt(),
                libido = (currentState.libido * 10).toInt(),
                note = currentState.notes,
                tags = emptyList()
            )
            addMoodUseCase(mood)
        }
    }
}

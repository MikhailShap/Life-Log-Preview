package com.lifelog.feature.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Entry
import com.lifelog.core.domain.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class TodayUiState(
    val mood: Int = 3,
    val energy: Float = 0.5f,
    val anxiety: Float = 0.5f,
    val notes: String = ""
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val entryRepository: EntryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    fun onMoodChange(mood: Int) {
        _uiState.value = _uiState.value.copy(mood = mood)
    }

    fun onEnergyChange(energy: Float) {
        _uiState.value = _uiState.value.copy(energy = energy)
    }

    fun onAnxietyChange(anxiety: Float) {
        _uiState.value = _uiState.value.copy(anxiety = anxiety)
    }

    fun onNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun saveEntry() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val entry = Entry(
                date = Date(),
                mood = currentState.mood,
                energy = (currentState.energy * 5).toInt(), // Scale to 0-5
                anxiety = (currentState.anxiety * 5).toInt(), // Scale to 0-5
                sleepHours = 0f, // Placeholder
                notes = currentState.notes,
                tags = emptyList(),
                videoNoteIds = emptyList()
            )
            entryRepository.saveEntry(entry)
        }
    }
}

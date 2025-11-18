package com.lifelog.feature.today

import android.util.Log
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

private const val TAG = "TodayViewModel"

data class TodayUiState(
    val sleepQuality: Int = 3, // 1-5
    val sleepStartTime: String = "23:00",
    val sleepEndTime: String = "07:00",
    val mood: Float = 5f, // Keep old fields just in case, or remove if not needed
    val anxiety: Float = 0.5f,
    val notes: String = ""
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val entryRepository: EntryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized")
    }

    fun onSleepQualityChange(quality: Int) {
        _uiState.value = _uiState.value.copy(sleepQuality = quality)
    }

    fun onSleepStartTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(sleepStartTime = time)
    }

    fun onSleepEndTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(sleepEndTime = time)
    }

    // Keep old methods if needed or refactor them
    fun onMoodChange(mood: Float) {
        _uiState.value = _uiState.value.copy(mood = mood)
    }

    fun onNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }
    
    fun onAnxietyChange(anxiety: Float) {
        _uiState.value = _uiState.value.copy(anxiety = anxiety)
    }

    fun saveEntry() {
        viewModelScope.launch {
            // TODO: Map new UI state to Entry model correctly
            val currentState = _uiState.value
             val entry = Entry(
                date = Date(),
                mood = currentState.sleepQuality, // Temporary mapping
                energy = 0, 
                anxiety = 0, 
                sleepHours = 8f, // Placeholder, calculate from times
                notes = currentState.notes,
                tags = emptyList(),
                videoNoteIds = emptyList()
            )
            entryRepository.saveEntry(entry)
        }
    }
}

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
    val sleepQuality: Int = 3, // 1-5 scale
    val sleepStartTime: String = "23:00",
    val sleepEndTime: String = "07:00",
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

    fun saveEntry() {
        Log.d(TAG, "saveEntry called")
        // Logic to save sleep entry will be implemented here
    }
}

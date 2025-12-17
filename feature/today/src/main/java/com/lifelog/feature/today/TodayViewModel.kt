package com.lifelog.feature.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.usecase.AddSleepUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

private const val TAG = "TodayViewModel"

data class TodayUiState(
    val sleepQuality: Int = 3, // 1-5
    val sleepStartTime: Long = 0L, // Timestamp
    val sleepEndTime: Long = 0L, // Timestamp
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val addSleepUseCase: AddSleepUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        // Initialize with default times (e.g., yesterday 23:00 and today 07:00)
        val calendar = Calendar.getInstance()
        
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        val defaultEnd = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        val defaultStart = calendar.timeInMillis

        _uiState.value = _uiState.value.copy(
            sleepStartTime = defaultStart,
            sleepEndTime = defaultEnd
        )
    }

    fun onSleepQualityChange(quality: Int) {
        _uiState.value = _uiState.value.copy(sleepQuality = quality)
    }

    fun onSleepStartTimeChange(time: Long) {
        _uiState.value = _uiState.value.copy(sleepStartTime = time)
    }

    fun onSleepEndTimeChange(time: Long) {
        _uiState.value = _uiState.value.copy(sleepEndTime = time)
    }

    fun showStartTimePicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showStartTimePicker = show)
    }

    fun showEndTimePicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showEndTimePicker = show)
    }

    fun saveEntry() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val sleep = Sleep(
                startTime = currentState.sleepStartTime,
                endTime = currentState.sleepEndTime,
                qualityRating = currentState.sleepQuality,
                notes = null
            )
            addSleepUseCase(sleep)
        }
    }
}

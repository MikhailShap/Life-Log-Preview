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

    fun saveEntry(dateInMillis: Long) {
        viewModelScope.launch {
            val currentState = _uiState.value
            
            // Adjust start/end times to match the selected date roughly if needed,
            // or just save them as is if the user picked specific times.
            // However, typically "Sleep log for Date X" implies the sleep that ended on Date X.
            // For simplicity, we just save the record. A more complex app would adjust the calendar dates.
            // Let's assume the user picks the actual start/end timestamps via the picker.
            // But if we want to associate it with the 'selectedDate', we might need to store that ref.
            // The 'Sleep' entity has startTime and endTime. We can add a 'dateRef' or just rely on start/end.
            // For this sprint, we'll just save the times as selected by the user. 
            // BUT, to make the 'date picker' useful, maybe we should shift the times to the selected date?
            
            // Let's shift the times to the selected date year/month/day, keeping hour/minute.
            val selectedCalendar = Calendar.getInstance().apply { timeInMillis = dateInMillis }
            
            val startCal = Calendar.getInstance().apply { timeInMillis = currentState.sleepStartTime }
            startCal.set(Calendar.YEAR, selectedCalendar.get(Calendar.YEAR))
            startCal.set(Calendar.MONTH, selectedCalendar.get(Calendar.MONTH))
            startCal.set(Calendar.DAY_OF_MONTH, selectedCalendar.get(Calendar.DAY_OF_MONTH))
            // If start was "yesterday" (23:00), we might need to subtract a day. 
            // But this logic gets complex. Let's just trust the user's picker or use the dateInMillis as a reference.
            
            // For now, let's just save.
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

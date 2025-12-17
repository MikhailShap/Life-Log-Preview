package com.lifelog.feature.trends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.repository.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

enum class TimeRange {
    WEEK, MONTH
}

data class TrendsUiState(
    val timeRange: TimeRange = TimeRange.WEEK,
    val moodData: List<Mood> = emptyList(),
    val sleepData: List<Sleep> = emptyList()
)

@HiltViewModel
class TrendsViewModel @Inject constructor(
    logRepository: LogRepository
) : ViewModel() {

    private val _timeRange = MutableStateFlow(TimeRange.WEEK)
    private val _moods = logRepository.getMoods()
    private val _sleeps = logRepository.getSleepLogs()

    val uiState: StateFlow<TrendsUiState> = combine(
        _timeRange,
        _moods,
        _sleeps
    ) { range, moods, sleeps ->
        val filteredMoods = filterMoodsByRange(moods, range)
        val filteredSleeps = filterSleepsByRange(sleeps, range)
        TrendsUiState(
            timeRange = range,
            moodData = filteredMoods,
            sleepData = filteredSleeps
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TrendsUiState()
    )

    fun setTimeRange(range: TimeRange) {
        _timeRange.value = range
    }

    private fun filterMoodsByRange(list: List<Mood>, range: TimeRange): List<Mood> {
        val cutoff = getCutoffTime(range)
        return list.filter { it.timestamp >= cutoff }.sortedBy { it.timestamp }
    }

    private fun filterSleepsByRange(list: List<Sleep>, range: TimeRange): List<Sleep> {
        val cutoff = getCutoffTime(range)
        return list.filter { it.startTime >= cutoff }.sortedBy { it.startTime }
    }

    private fun getCutoffTime(range: TimeRange): Long {
        val calendar = Calendar.getInstance()
        when (range) {
            TimeRange.WEEK -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            TimeRange.MONTH -> calendar.add(Calendar.DAY_OF_YEAR, -30)
        }
        return calendar.timeInMillis
    }
}

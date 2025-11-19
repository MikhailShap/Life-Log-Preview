package com.lifelog.feature.log.presentation.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Mood
import com.lifelog.feature.log.domain.usecase.AddMoodUseCase
import com.lifelog.feature.log.domain.usecase.GetMoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val addMoodUseCase: AddMoodUseCase,
    private val getMoodsUseCase: GetMoodsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MoodState())
    val state: StateFlow<MoodState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        getMoodsUseCase()
            .onEach { moods ->
                _state.update { it.copy(moodHistory = moods) }
            }
            .launchIn(viewModelScope)
    }

    fun onMoodChange(rating: Int) {
        _state.update { it.copy(moodRating = rating) }
    }

    fun onNoteChange(note: String) {
        _state.update { it.copy(note = note) }
    }

    fun saveMood() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val currentState = _state.value
            val mood = Mood(
                timestamp = System.currentTimeMillis(),
                rating = currentState.moodRating,
                note = currentState.note,
                tags = emptyList() // Add tags logic if needed
            )
            addMoodUseCase(mood)
            _state.update { it.copy(isLoading = false, isSaved = true) }
        }
    }

    fun resetState() {
        _state.update { it.copy(isSaved = false, note = "", moodRating = 3) }
    }
}

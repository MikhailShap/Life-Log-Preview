package com.lifelog.feature.log.presentation.mood

import com.lifelog.core.domain.model.Mood

data class MoodState(
    val isLoading: Boolean = false,
    val moodRating: Int = 3,
    val note: String = "",
    val isSaved: Boolean = false,
    val moodHistory: List<Mood> = emptyList()
)

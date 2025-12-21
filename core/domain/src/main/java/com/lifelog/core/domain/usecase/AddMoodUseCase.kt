package com.lifelog.core.domain.usecase

import android.util.Log
import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.repository.LogRepository
import javax.inject.Inject

private const val TAG = "AddMoodUseCase"

class AddMoodUseCase @Inject constructor(
    private val repository: LogRepository
) {
    suspend operator fun invoke(mood: Mood) {
        try {
            repository.saveMood(mood)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add mood entry", e)
            throw IllegalStateException("Unable to save mood entry. Please try again.", e)
        }
    }
}

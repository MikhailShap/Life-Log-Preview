package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.repository.LogRepository
import javax.inject.Inject

class AddMoodUseCase @Inject constructor(
    private val repository: LogRepository
) {
    suspend operator fun invoke(mood: Mood) {
        repository.saveMood(mood)
    }
}

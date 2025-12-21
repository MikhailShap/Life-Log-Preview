package com.lifelog.core.domain.usecase

import android.util.Log
import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.repository.LogRepository
import javax.inject.Inject

private const val TAG = "AddSleepUseCase"

class AddSleepUseCase @Inject constructor(
    private val repository: LogRepository
) {
    suspend operator fun invoke(sleep: Sleep) {
        try {
            repository.saveSleep(sleep)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add sleep entry", e)
            throw IllegalStateException("Unable to save sleep entry. Please try again.", e)
        }
    }
}

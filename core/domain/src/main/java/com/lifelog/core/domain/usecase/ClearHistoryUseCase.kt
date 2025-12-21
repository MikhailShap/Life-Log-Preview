package com.lifelog.core.domain.usecase

import android.util.Log
import com.lifelog.core.domain.repository.LogRepository
import javax.inject.Inject

private const val TAG = "ClearHistoryUseCase"

class ClearHistoryUseCase @Inject constructor(
    private val logRepository: LogRepository
) {
    suspend operator fun invoke() {
        try {
            logRepository.clearAllLogs()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear history", e)
            throw IllegalStateException("Unable to clear history. Please try again.", e)
        }
    }
}

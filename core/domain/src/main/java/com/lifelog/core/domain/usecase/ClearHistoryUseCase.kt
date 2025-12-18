package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.repository.LogRepository
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val logRepository: LogRepository
) {
    suspend operator fun invoke() {
        logRepository.clearAllLogs()
    }
}

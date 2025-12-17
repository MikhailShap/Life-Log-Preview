package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSleepLogsUseCase @Inject constructor(
    private val repository: LogRepository
) {
    operator fun invoke(): Flow<List<Sleep>> {
        return repository.getSleepLogs()
    }
}

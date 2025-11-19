package com.lifelog.feature.log.domain.usecase

import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.repository.LogRepository
import javax.inject.Inject

class AddSleepUseCase @Inject constructor(
    private val repository: LogRepository
) {
    suspend operator fun invoke(sleep: Sleep) {
        repository.saveSleep(sleep)
    }
}

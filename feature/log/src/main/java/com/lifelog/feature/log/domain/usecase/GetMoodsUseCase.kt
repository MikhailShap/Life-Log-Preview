package com.lifelog.feature.log.domain.usecase

import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoodsUseCase @Inject constructor(
    private val repository: LogRepository
) {
    operator fun invoke(): Flow<List<Mood>> {
        return repository.getMoods()
    }
}

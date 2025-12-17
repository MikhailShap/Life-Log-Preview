package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.repository.SideEffectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSideEffectsUseCase @Inject constructor(
    private val repository: SideEffectRepository
) {
    operator fun invoke(): Flow<List<SideEffect>> {
        return repository.getAllSideEffects()
    }
}

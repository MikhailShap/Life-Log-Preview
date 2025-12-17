package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.repository.SideEffectRepository
import javax.inject.Inject

class AddSideEffectUseCase @Inject constructor(
    private val repository: SideEffectRepository
) {
    suspend operator fun invoke(sideEffect: SideEffect) {
        repository.saveSideEffect(sideEffect)
    }
}

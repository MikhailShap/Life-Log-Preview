package com.lifelog.core.domain.usecase

import android.util.Log
import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.repository.SideEffectRepository
import javax.inject.Inject

private const val TAG = "DeleteSideEffectUseCase"

class DeleteSideEffectUseCase @Inject constructor(
    private val repository: SideEffectRepository
) {
    suspend operator fun invoke(sideEffect: SideEffect) {
        try {
            repository.deleteSideEffect(sideEffect)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete side effect", e)
            throw IllegalStateException("Unable to delete side effect. Please try again.", e)
        }
    }
}

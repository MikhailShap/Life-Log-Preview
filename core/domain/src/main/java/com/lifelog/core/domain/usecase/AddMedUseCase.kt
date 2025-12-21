package com.lifelog.core.domain.usecase

import android.util.Log
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import javax.inject.Inject

private const val TAG = "AddMedUseCase"

class AddMedUseCase @Inject constructor(
    private val repository: MedRepository
) {
    suspend operator fun invoke(med: Med) {
        try {
            repository.saveMed(med)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add medication", e)
            throw IllegalStateException("Unable to save medication. Please try again.", e)
        }
    }
}

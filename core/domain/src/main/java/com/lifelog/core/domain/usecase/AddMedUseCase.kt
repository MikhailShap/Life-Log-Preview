package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import javax.inject.Inject

class AddMedUseCase @Inject constructor(
    private val repository: MedRepository
) {
    suspend operator fun invoke(med: Med) {
        repository.saveMed(med)
    }
}

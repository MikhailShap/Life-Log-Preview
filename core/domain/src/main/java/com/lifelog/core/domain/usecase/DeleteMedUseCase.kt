package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.repository.MedRepository
import javax.inject.Inject

class DeleteMedUseCase @Inject constructor(
    private val repository: MedRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteMedById(id)
    }
}

package com.lifelog.core.domain.usecase

import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedsUseCase @Inject constructor(
    private val repository: MedRepository
) {
    operator fun invoke(): Flow<List<Med>> {
        return repository.getAllMeds()
    }
}

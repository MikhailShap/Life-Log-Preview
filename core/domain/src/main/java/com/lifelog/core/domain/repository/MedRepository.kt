package com.lifelog.core.domain.repository

import com.lifelog.core.domain.model.Med
import kotlinx.coroutines.flow.Flow

interface MedRepository {
    suspend fun saveMed(med: Med)
    fun getAllMeds(): Flow<List<Med>>
    suspend fun deleteMedById(id: Long)
}

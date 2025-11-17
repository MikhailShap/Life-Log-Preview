package com.lifelog.core.data.repository

import com.lifelog.core.data.local.dao.MedDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedRepositoryImpl @Inject constructor(
    private val medDao: MedDao
) : MedRepository {
    override suspend fun saveMed(med: Med) {
        medDao.insert(med.toEntity())
    }

    override fun getAllMeds(): Flow<List<Med>> {
        return medDao.getAllMeds().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteMedById(id: Long) {
        medDao.deleteMedById(id)
    }
}

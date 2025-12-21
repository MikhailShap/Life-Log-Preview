package com.lifelog.core.data.repository

import android.util.Log
import com.lifelog.core.data.local.dao.MedDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "MedRepositoryImpl"

class MedRepositoryImpl @Inject constructor(
    private val medDao: MedDao
) : MedRepository {
    override suspend fun saveMed(med: Med) {
        try {
            medDao.insert(med.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save medication", e)
            throw IllegalStateException("Failed to save medication: ${e.message}", e)
        }
    }

    override fun getAllMeds(): Flow<List<Med>> {
        return medDao.getAllMeds()
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve medications", e)
                throw IllegalStateException("Failed to retrieve medications: ${e.message}", e)
            }
    }

    override suspend fun deleteMedById(id: Long) {
        try {
            medDao.deleteMedById(id)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete medication with id: $id", e)
            throw IllegalStateException("Failed to delete medication: ${e.message}", e)
        }
    }
}

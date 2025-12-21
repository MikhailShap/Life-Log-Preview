package com.lifelog.core.data.repository

import android.util.Log
import com.lifelog.core.data.local.dao.SideEffectDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.repository.SideEffectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "SideEffectRepositoryImpl"

class SideEffectRepositoryImpl @Inject constructor(
    private val dao: SideEffectDao
) : SideEffectRepository {
    override fun getAllSideEffects(): Flow<List<SideEffect>> {
        return dao.getAllSideEffects()
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve side effects", e)
                throw IllegalStateException("Failed to retrieve side effects: ${e.message}", e)
            }
    }

    override suspend fun saveSideEffect(sideEffect: SideEffect) {
        try {
            dao.insertSideEffect(sideEffect.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save side effect", e)
            throw IllegalStateException("Failed to save side effect: ${e.message}", e)
        }
    }

    override suspend fun deleteSideEffect(sideEffect: SideEffect) {
        try {
            dao.deleteSideEffect(sideEffect.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete side effect", e)
            throw IllegalStateException("Failed to delete side effect: ${e.message}", e)
        }
    }
}

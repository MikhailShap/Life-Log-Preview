package com.lifelog.core.data.repository

import com.lifelog.core.data.local.dao.SideEffectDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.repository.SideEffectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SideEffectRepositoryImpl @Inject constructor(
    private val dao: SideEffectDao
) : SideEffectRepository {
    override fun getAllSideEffects(): Flow<List<SideEffect>> {
        return dao.getAllSideEffects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveSideEffect(sideEffect: SideEffect) {
        dao.insertSideEffect(sideEffect.toEntity())
    }

    override suspend fun deleteSideEffect(sideEffect: SideEffect) {
        dao.deleteSideEffect(sideEffect.toEntity())
    }
}

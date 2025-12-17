package com.lifelog.core.domain.repository

import com.lifelog.core.domain.model.SideEffect
import kotlinx.coroutines.flow.Flow

interface SideEffectRepository {
    fun getAllSideEffects(): Flow<List<SideEffect>>
    suspend fun saveSideEffect(sideEffect: SideEffect)
    suspend fun deleteSideEffect(sideEffect: SideEffect)
}

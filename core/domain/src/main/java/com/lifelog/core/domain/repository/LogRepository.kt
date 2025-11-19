package com.lifelog.core.domain.repository

import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.model.Sleep
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    suspend fun saveMood(mood: Mood)
    fun getMoods(): Flow<List<Mood>>
    suspend fun saveSleep(sleep: Sleep)
    fun getSleepLogs(): Flow<List<Sleep>>
}

package com.lifelog.core.data.repository

import android.util.Log
import com.lifelog.core.data.local.dao.MoodDao
import com.lifelog.core.data.local.dao.SleepDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "LogRepositoryImpl"

class LogRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao,
    private val sleepDao: SleepDao
) : LogRepository {

    override suspend fun saveMood(mood: Mood) {
        try {
            moodDao.insertMood(mood.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save mood entry", e)
            throw IllegalStateException("Failed to save mood entry: ${e.message}", e)
        }
    }

    override fun getMoods(): Flow<List<Mood>> {
        return moodDao.getAllMoods()
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve mood entries", e)
                throw IllegalStateException("Failed to retrieve mood entries: ${e.message}", e)
            }
    }

    override suspend fun saveSleep(sleep: Sleep) {
        try {
            sleepDao.insertSleep(sleep.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save sleep entry", e)
            throw IllegalStateException("Failed to save sleep entry: ${e.message}", e)
        }
    }

    override fun getSleepLogs(): Flow<List<Sleep>> {
        return sleepDao.getAllSleep()
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve sleep logs", e)
                throw IllegalStateException("Failed to retrieve sleep logs: ${e.message}", e)
            }
    }

    override suspend fun clearAllLogs() {
        try {
            moodDao.clearAllMoods()
            sleepDao.clearAllSleep()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear all logs", e)
            throw IllegalStateException("Failed to clear all logs: ${e.message}", e)
        }
    }
}

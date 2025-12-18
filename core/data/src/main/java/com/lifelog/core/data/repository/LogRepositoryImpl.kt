package com.lifelog.core.data.repository

import com.lifelog.core.data.local.dao.MoodDao
import com.lifelog.core.data.local.dao.SleepDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao,
    private val sleepDao: SleepDao
) : LogRepository {

    override suspend fun saveMood(mood: Mood) {
        moodDao.insertMood(mood.toEntity())
    }

    override fun getMoods(): Flow<List<Mood>> {
        return moodDao.getAllMoods().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveSleep(sleep: Sleep) {
        sleepDao.insertSleep(sleep.toEntity())
    }

    override fun getSleepLogs(): Flow<List<Sleep>> {
        return sleepDao.getAllSleep().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun clearAllLogs() {
        moodDao.clearAllMoods()
        sleepDao.clearAllSleep()
    }
}

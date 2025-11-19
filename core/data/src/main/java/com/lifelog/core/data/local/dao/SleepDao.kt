package com.lifelog.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifelog.core.data.local.entity.SleepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleep(sleep: SleepEntity)

    @Query("SELECT * FROM sleep_table ORDER BY startTime DESC")
    fun getAllSleep(): Flow<List<SleepEntity>>

    @Delete
    suspend fun deleteSleep(sleep: SleepEntity)
}

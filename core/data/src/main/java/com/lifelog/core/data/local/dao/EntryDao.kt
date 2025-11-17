package com.lifelog.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifelog.core.data.local.entity.EntryEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: EntryEntity)

    @Query("SELECT * FROM entries WHERE date = :date")
    fun getEntryForDate(date: Date): Flow<EntryEntity?>

    @Query("SELECT * FROM entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<EntryEntity>>
}

package com.lifelog.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifelog.core.data.local.entity.VideoNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(videoNote: VideoNoteEntity)

    @Query("SELECT * FROM video_notes ORDER BY createdAt DESC")
    fun getAllVideoNotes(): Flow<List<VideoNoteEntity>>
}

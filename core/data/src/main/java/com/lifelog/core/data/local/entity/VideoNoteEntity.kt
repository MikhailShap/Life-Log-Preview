package com.lifelog.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "video_notes")
data class VideoNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val createdAt: Date,
    val uri: String,
    val thumbUri: String?,
    val duration: Long
)

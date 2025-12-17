package com.lifelog.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_table")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val rating: Int,
    val energy: Int = 0, // Added
    val stress: Int = 0, // Added
    val libido: Int = 0, // Added
    val note: String?,
    val tags: List<String>
)

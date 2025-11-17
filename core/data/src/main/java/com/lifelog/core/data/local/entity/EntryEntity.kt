package com.lifelog.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "entries")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val mood: Int,
    val energy: Int,
    val anxiety: Int,
    val sleepHours: Float,
    val notes: String?,
    val tags: List<String>,
    val videoNoteIds: List<Long>
)

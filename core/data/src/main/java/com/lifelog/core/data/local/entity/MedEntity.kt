package com.lifelog.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meds")
data class MedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val timeOfDay: String // e.g., "Morning", "Evening"
)

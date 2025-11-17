package com.lifelog.core.domain.model

import java.util.Date

data class Entry(
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

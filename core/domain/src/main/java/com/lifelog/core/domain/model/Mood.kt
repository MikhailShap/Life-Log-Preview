package com.lifelog.core.domain.model

data class Mood(
    val id: Long = 0,
    val timestamp: Long,
    val rating: Int,
    val note: String?,
    val tags: List<String>
)

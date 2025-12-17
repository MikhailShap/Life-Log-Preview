package com.lifelog.core.domain.model

data class Mood(
    val id: Long = 0,
    val timestamp: Long,
    val rating: Int,
    val energy: Int = 0,
    val stress: Int = 0,
    val libido: Int = 0,
    val note: String?,
    val tags: List<String>
)

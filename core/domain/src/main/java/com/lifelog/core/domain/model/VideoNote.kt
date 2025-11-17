package com.lifelog.core.domain.model

import java.util.Date

data class VideoNote(
    val id: Long = 0,
    val createdAt: Date,
    val uri: String,
    val thumbUri: String?,
    val duration: Long
)

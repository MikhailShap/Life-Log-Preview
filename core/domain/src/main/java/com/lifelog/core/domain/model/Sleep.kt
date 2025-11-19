package com.lifelog.core.domain.model

data class Sleep(
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val qualityRating: Int,
    val notes: String?
) {
    val duration: Long
        get() = endTime - startTime
}

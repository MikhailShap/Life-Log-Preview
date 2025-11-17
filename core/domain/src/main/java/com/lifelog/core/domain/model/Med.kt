package com.lifelog.core.domain.model

data class Med(
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val timeOfDay: String
)

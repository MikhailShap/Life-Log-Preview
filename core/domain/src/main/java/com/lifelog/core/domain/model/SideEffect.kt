package com.lifelog.core.domain.model

data class SideEffect(
    val id: Long = 0,
    val name: String,
    val frequency: String,
    val date: Long
)

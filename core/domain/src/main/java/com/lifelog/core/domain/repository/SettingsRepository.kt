package com.lifelog.core.domain.repository

import com.lifelog.core.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<ThemeMode>
    val language: Flow<String>

    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setLanguage(language: String)
}

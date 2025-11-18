package com.lifelog.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
    }

    override val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        ThemeMode.valueOf(preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name)
    }

    override val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LANGUAGE] ?: "en"
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    override suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }
}

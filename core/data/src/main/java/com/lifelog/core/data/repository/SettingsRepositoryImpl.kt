package com.lifelog.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
    }

    override val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { preferences ->
            ThemeMode.valueOf(
                preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
            )
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    override val language: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE] ?: "en" // Default to English
        }

    override suspend fun setLanguage(language: String) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.LANGUAGE] = language
        }
    }
}

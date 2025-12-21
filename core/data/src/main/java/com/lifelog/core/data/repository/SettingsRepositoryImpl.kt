package com.lifelog.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "SettingsRepositoryImpl"

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
    }

    override val themeMode: Flow<ThemeMode> = dataStore.data
        .map { preferences ->
            try {
                ThemeMode.valueOf(preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Invalid theme mode in preferences, defaulting to SYSTEM", e)
                ThemeMode.SYSTEM
            }
        }
        .catch { e ->
            Log.e(TAG, "Failed to read theme mode from DataStore", e)
            emit(ThemeMode.SYSTEM)
        }

    override val language: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE] ?: "en"
        }
        .catch { e ->
            Log.e(TAG, "Failed to read language from DataStore", e)
            emit("en")
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.THEME_MODE] = themeMode.name
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save theme mode to DataStore", e)
            throw IllegalStateException("Failed to save theme mode: ${e.message}", e)
        }
    }

    override suspend fun setLanguage(language: String) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.LANGUAGE] = language
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save language to DataStore", e)
            throw IllegalStateException("Failed to save language: ${e.message}", e)
        }
    }
}

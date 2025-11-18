package com.lifelog.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SettingsViewModel"

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: String = "en"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        combine(settingsRepository.themeMode, settingsRepository.language) { theme, lang ->
            Log.d(TAG, "uiState updated: theme=$theme, lang=$lang")
            SettingsUiState(themeMode = theme, language = lang)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            Log.d(TAG, "setThemeMode called with: $themeMode")
            settingsRepository.setThemeMode(themeMode)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            Log.d(TAG, "setLanguage called with: $language")
            settingsRepository.setLanguage(language)
        }
    }
}

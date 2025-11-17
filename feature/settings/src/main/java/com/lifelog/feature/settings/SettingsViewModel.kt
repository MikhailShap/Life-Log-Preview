package com.lifelog.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: String = "en"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        settingsRepository.themeMode.map { themeMode ->
            SettingsUiState(themeMode = themeMode, language = settingsRepository.language.replayCache.firstOrNull() ?: "en")
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(themeMode)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }
}

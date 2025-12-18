package com.lifelog.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.domain.repository.SettingsRepository
import com.lifelog.core.domain.usecase.ClearHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: String = "en"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        combine(settingsRepository.themeMode, settingsRepository.language) { theme, lang ->
            SettingsUiState(themeMode = theme, language = lang)
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
        val appLocale = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }

    fun exportData() {
        // Placeholder for future export logic
    }

    fun clearHistory() {
        viewModelScope.launch {
            clearHistoryUseCase()
        }
    }
}

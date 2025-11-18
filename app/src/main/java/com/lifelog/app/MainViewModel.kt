package com.lifelog.app

import androidx.lifecycle.ViewModel
import com.lifelog.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val themeMode = settingsRepository.themeMode
    val language = settingsRepository.language
}

package com.lifelog.app

import androidx.lifecycle.ViewModel
import com.lifelog.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val themeMode = settingsRepository.themeMode
    val language = settingsRepository.language
    
    // Persist current sub-screen in Log section
    private val _currentLogSubScreen = MutableStateFlow("MOOD")
    val currentLogSubScreen = _currentLogSubScreen.asStateFlow()
    
    fun setLogSubScreen(screenName: String) {
        _currentLogSubScreen.value = screenName
    }
}

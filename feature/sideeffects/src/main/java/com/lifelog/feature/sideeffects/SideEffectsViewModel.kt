package com.lifelog.feature.sideeffects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.usecase.AddSideEffectUseCase
import com.lifelog.core.domain.usecase.DeleteSideEffectUseCase
import com.lifelog.core.domain.usecase.GetSideEffectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SideEffectsViewModel"

@HiltViewModel
class SideEffectsViewModel @Inject constructor(
    private val getSideEffectsUseCase: GetSideEffectsUseCase,
    private val addSideEffectUseCase: AddSideEffectUseCase,
    private val deleteSideEffectUseCase: DeleteSideEffectUseCase
) : ViewModel() {

    val sideEffects: StateFlow<List<SideEffect>> = getSideEffectsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun addSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            try {
                addSideEffectUseCase(sideEffect)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add side effect", e)
                _errorMessage.value = e.message ?: "Failed to add side effect"
            }
        }
    }

    fun deleteSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            try {
                deleteSideEffectUseCase(sideEffect)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete side effect", e)
                _errorMessage.value = e.message ?: "Failed to delete side effect"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

package com.lifelog.feature.sideeffects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.domain.usecase.AddSideEffectUseCase
import com.lifelog.core.domain.usecase.DeleteSideEffectUseCase
import com.lifelog.core.domain.usecase.GetSideEffectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideEffectsViewModel @Inject constructor(
    private val getSideEffectsUseCase: GetSideEffectsUseCase,
    private val addSideEffectUseCase: AddSideEffectUseCase,
    private val deleteSideEffectUseCase: DeleteSideEffectUseCase
) : ViewModel() {

    val sideEffects: StateFlow<List<SideEffect>> = getSideEffectsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            addSideEffectUseCase(sideEffect)
        }
    }

    fun deleteSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            deleteSideEffectUseCase(sideEffect)
        }
    }
}

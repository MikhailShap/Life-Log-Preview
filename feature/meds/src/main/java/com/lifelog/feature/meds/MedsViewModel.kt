package com.lifelog.feature.meds

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.usecase.AddMedUseCase
import com.lifelog.core.domain.usecase.DeleteMedUseCase
import com.lifelog.core.domain.usecase.GetMedsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MedsViewModel"

@HiltViewModel
class MedsViewModel @Inject constructor(
    private val getMedsUseCase: GetMedsUseCase,
    private val addMedUseCase: AddMedUseCase,
    private val deleteMedUseCase: DeleteMedUseCase
) : ViewModel() {

    val meds: StateFlow<List<Med>> = getMedsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun addMed(med: Med) {
        viewModelScope.launch {
            try {
                addMedUseCase(med)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add medication", e)
                _errorMessage.value = e.message ?: "Failed to add medication"
            }
        }
    }

    fun deleteMed(id: Long) {
        viewModelScope.launch {
            try {
                deleteMedUseCase(id)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete medication", e)
                _errorMessage.value = e.message ?: "Failed to delete medication"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

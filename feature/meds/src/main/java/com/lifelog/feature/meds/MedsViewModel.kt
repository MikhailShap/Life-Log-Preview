package com.lifelog.feature.meds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.usecase.AddMedUseCase
import com.lifelog.core.domain.usecase.DeleteMedUseCase
import com.lifelog.core.domain.usecase.GetMedsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedsViewModel @Inject constructor(
    private val getMedsUseCase: GetMedsUseCase,
    private val addMedUseCase: AddMedUseCase,
    private val deleteMedUseCase: DeleteMedUseCase
) : ViewModel() {

    val meds: StateFlow<List<Med>> = getMedsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMed(med: Med) {
        viewModelScope.launch {
            addMedUseCase(med)
        }
    }

    fun deleteMed(id: Long) {
        viewModelScope.launch {
            deleteMedUseCase(id)
        }
    }
}

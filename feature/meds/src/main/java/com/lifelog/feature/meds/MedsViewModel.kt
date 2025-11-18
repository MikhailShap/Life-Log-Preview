package com.lifelog.feature.meds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedsViewModel @Inject constructor(
    private val medRepository: MedRepository
) : ViewModel() {

    val meds: StateFlow<List<Med>> = medRepository.getAllMeds()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveMed(name: String, dosage: String, time: String) {
        viewModelScope.launch {
            val med = Med(name = name, dosage = dosage, timeOfDay = time)
            medRepository.saveMed(med)
        }
    }

    fun deleteMed(med: Med) {
        viewModelScope.launch {
            medRepository.deleteMedById(med.id)
        }
    }
}

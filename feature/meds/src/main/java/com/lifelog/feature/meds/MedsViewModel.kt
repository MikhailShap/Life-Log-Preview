package com.lifelog.feature.meds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.Med
import com.lifelog.core.domain.repository.MedRepository
import com.lifelog.feature.meds.reminders.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedsViewModel @Inject constructor(
    private val medRepository: MedRepository,
    private val reminderManager: ReminderManager
) : ViewModel() {

    val meds = medRepository.getAllMeds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveMed(name: String, dosage: String, timeOfDay: String) {
        viewModelScope.launch {
            val med = Med(name = name, dosage = dosage, timeOfDay = timeOfDay)
            medRepository.saveMed(med)
            reminderManager.scheduleReminder(med)
        }
    }

    fun deleteMed(med: Med) {
        viewModelScope.launch {
            medRepository.deleteMedById(med.id)
            reminderManager.cancelReminder(med)
        }
    }
}

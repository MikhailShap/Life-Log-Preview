package com.lifelog.feature.log

import androidx.lifecycle.ViewModel
import com.lifelog.core.domain.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val entryRepository: EntryRepository
) : ViewModel() {
    val entries = entryRepository.getAllEntries()
}

package com.lifelog.feature.trends

import androidx.lifecycle.ViewModel
import com.lifelog.core.domain.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val entryRepository: EntryRepository
) : ViewModel() {
    val entries = entryRepository.getAllEntries()
}

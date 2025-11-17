package com.lifelog.core.domain.repository

import com.lifelog.core.domain.model.Entry
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface EntryRepository {
    suspend fun saveEntry(entry: Entry)
    fun getEntryForDate(date: Date): Flow<Entry?>
    fun getAllEntries(): Flow<List<Entry>>
}

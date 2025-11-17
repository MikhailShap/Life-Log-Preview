package com.lifelog.core.data.repository

import com.lifelog.core.data.local.dao.EntryDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.Entry
import com.lifelog.core.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class EntryRepositoryImpl @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {
    override suspend fun saveEntry(entry: Entry) {
        entryDao.insert(entry.toEntity())
    }

    override fun getEntryForDate(date: Date): Flow<Entry?> {
        return entryDao.getEntryForDate(date).map { it?.toDomain() }
    }

    override fun getAllEntries(): Flow<List<Entry>> {
        return entryDao.getAllEntries().map { list ->
            list.map { it.toDomain() }
        }
    }
}

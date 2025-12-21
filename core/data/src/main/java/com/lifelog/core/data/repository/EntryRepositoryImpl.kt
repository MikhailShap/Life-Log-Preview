package com.lifelog.core.data.repository

import android.util.Log
import com.lifelog.core.data.local.dao.EntryDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.Entry
import com.lifelog.core.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

private const val TAG = "EntryRepositoryImpl"

class EntryRepositoryImpl @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {
    override suspend fun saveEntry(entry: Entry) {
        try {
            entryDao.insert(entry.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save entry", e)
            throw IllegalStateException("Failed to save entry: ${e.message}", e)
        }
    }

    override fun getEntryForDate(date: Date): Flow<Entry?> {
        return entryDao.getEntryForDate(date)
            .map { it?.toDomain() }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve entry for date: $date", e)
                throw IllegalStateException("Failed to retrieve entry: ${e.message}", e)
            }
    }

    override fun getAllEntries(): Flow<List<Entry>> {
        return entryDao.getAllEntries()
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve all entries", e)
                throw IllegalStateException("Failed to retrieve entries: ${e.message}", e)
            }
    }
}

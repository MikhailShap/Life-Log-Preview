package com.lifelog.core.data.repository

import android.util.Log
import com.lifelog.core.data.local.dao.VideoNoteDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.VideoNote
import com.lifelog.core.domain.repository.VideoNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "VideoNoteRepositoryImpl"

class VideoNoteRepositoryImpl @Inject constructor(
    private val videoNoteDao: VideoNoteDao
) : VideoNoteRepository {
    override suspend fun saveVideoNote(videoNote: VideoNote) {
        try {
            videoNoteDao.insert(videoNote.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save video note", e)
            throw IllegalStateException("Failed to save video note: ${e.message}", e)
        }
    }

    override fun getAllVideoNotes(): Flow<List<VideoNote>> {
        return videoNoteDao.getAllVideoNotes()
            .map { list -> list.map { it.toDomain() } }
            .catch { e ->
                Log.e(TAG, "Failed to retrieve video notes", e)
                throw IllegalStateException("Failed to retrieve video notes: ${e.message}", e)
            }
    }
}

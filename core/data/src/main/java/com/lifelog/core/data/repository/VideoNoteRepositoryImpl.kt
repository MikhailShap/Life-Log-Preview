package com.lifelog.core.data.repository

import com.lifelog.core.data.local.dao.VideoNoteDao
import com.lifelog.core.data.mapper.toDomain
import com.lifelog.core.data.mapper.toEntity
import com.lifelog.core.domain.model.VideoNote
import com.lifelog.core.domain.repository.VideoNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoNoteRepositoryImpl @Inject constructor(
    private val videoNoteDao: VideoNoteDao
) : VideoNoteRepository {
    override suspend fun saveVideoNote(videoNote: VideoNote) {
        videoNoteDao.insert(videoNote.toEntity())
    }

    override fun getAllVideoNotes(): Flow<List<VideoNote>> {
        return videoNoteDao.getAllVideoNotes().map { list ->
            list.map { it.toDomain() }
        }
    }
}

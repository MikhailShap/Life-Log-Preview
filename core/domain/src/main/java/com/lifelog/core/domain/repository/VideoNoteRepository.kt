package com.lifelog.core.domain.repository

import com.lifelog.core.domain.model.VideoNote
import kotlinx.coroutines.flow.Flow

interface VideoNoteRepository {
    suspend fun saveVideoNote(videoNote: VideoNote)
    fun getAllVideoNotes(): Flow<List<VideoNote>>
}

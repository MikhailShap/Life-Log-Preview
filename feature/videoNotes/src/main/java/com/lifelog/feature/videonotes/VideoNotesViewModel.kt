package com.lifelog.feature.videonotes

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.VideoNote
import com.lifelog.core.domain.repository.VideoNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class VideoNotesViewModel @Inject constructor(
    private val videoNoteRepository: VideoNoteRepository
) : ViewModel() {

    val videoNotes: StateFlow<List<VideoNote>> = videoNoteRepository.getAllVideoNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveVideoNote(uri: Uri, duration: Long) {
        viewModelScope.launch {
            val videoNote = VideoNote(
                createdAt = Date(),
                uri = uri.toString(),
                thumbUri = null, // TODO: Generate thumbnail
                duration = duration
            )
            videoNoteRepository.saveVideoNote(videoNote)
        }
    }
}

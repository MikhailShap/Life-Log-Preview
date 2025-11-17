package com.lifelog.feature.videonotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.VideoNote
import com.lifelog.core.domain.repository.VideoNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class VideoNotesViewModel @Inject constructor(
    private val videoNoteRepository: VideoNoteRepository
) : ViewModel() {

    val videoNotes = videoNoteRepository.getAllVideoNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveVideoNote(uri: String, duration: Long) {
        viewModelScope.launch {
            val videoNote = VideoNote(
                createdAt = Date(),
                uri = uri,
                thumbUri = null, // Placeholder for thumbnail generation
                duration = duration
            )
            videoNoteRepository.saveVideoNote(videoNote)
        }
    }
}

package com.lifelog.feature.videonotes

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelog.core.domain.model.VideoNote
import com.lifelog.core.domain.repository.VideoNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private const val TAG = "VideoNotesViewModel"

@HiltViewModel
class VideoNotesViewModel @Inject constructor(
    private val videoNoteRepository: VideoNoteRepository
) : ViewModel() {

    val videoNotes: StateFlow<List<VideoNote>> = videoNoteRepository.getAllVideoNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun saveVideoNote(uri: Uri, duration: Long) {
        viewModelScope.launch {
            try {
                val videoNote = VideoNote(
                    createdAt = Date(),
                    uri = uri.toString(),
                    thumbUri = null, // TODO: Generate thumbnail
                    duration = duration
                )
                videoNoteRepository.saveVideoNote(videoNote)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save video note", e)
                _errorMessage.value = e.message ?: "Failed to save video note"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

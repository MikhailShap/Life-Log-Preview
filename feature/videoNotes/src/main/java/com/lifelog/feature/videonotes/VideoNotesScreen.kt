package com.lifelog.feature.videonotes

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.VideoNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoNotesScreen(
    viewModel: VideoNotesViewModel = hiltViewModel(),
    onNavigateToRecord: () -> Unit
) {
    val videoNotes by viewModel.videoNotes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Video Notes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToRecord) {
                Icon(Icons.Default.Add, contentDescription = "Record Video Note")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(videoNotes) { videoNote ->
                VideoNoteItem(videoNote)
            }
        }
    }
}

@Composable
fun VideoNoteItem(videoNote: VideoNote) {
    // Placeholder for video thumbnail
    Text("Video from ${videoNote.createdAt}")
}

@Preview
@Composable
fun VideoNotesScreenPreview() {
    // Preview won't show much without a proper ViewModel
}

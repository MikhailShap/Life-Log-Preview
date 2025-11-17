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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.VideoNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoNotesScreen(
    onAddVideo: () -> Unit,
    viewModel: VideoNotesViewModel = hiltViewModel()
) {
    val videoNotes by viewModel.videoNotes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Video Notes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddVideo) {
                Icon(Icons.Default.Add, contentDescription = "Add Video Note")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier.padding(paddingValues).padding(4.dp)
        ) {
            items(videoNotes) { videoNote ->
                VideoNoteItem(videoNote = videoNote)
            }
        }
    }
}

@Composable
fun VideoNoteItem(videoNote: VideoNote) {
    // For simplicity, just display the creation date
    Text(
        text = videoNote.createdAt.toString(),
        modifier = Modifier.padding(8.dp)
    )
}

@Preview
@Composable
fun VideoNotesScreenPreview() {
    VideoNotesScreen(onAddVideo = {})
}

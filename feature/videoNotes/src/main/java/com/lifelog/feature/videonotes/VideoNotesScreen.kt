package com.lifelog.feature.videonotes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VideoNotesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Video Notes",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(modifier = Modifier.padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Record circular video clips with CameraX and audio input.", style = MaterialTheme.typography.bodyMedium)
                Text("Show thumbnails and allow playback via Media3/ExoPlayer.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Handle CAMERA and RECORD_AUDIO permissions with rationale dialogs.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

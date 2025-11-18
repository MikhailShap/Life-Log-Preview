package com.lifelog.feature.today

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
fun TodayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Today",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(modifier = Modifier.padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mood, energy, anxiety, sleep sliders and emoji selection will appear here.", style = MaterialTheme.typography.bodyMedium)
                Text("Changes should auto-save to the local database.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Card(modifier = Modifier.padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add note text and attach a video note.", style = MaterialTheme.typography.bodyMedium)
                Text("Use CameraX for recording and Media3 for playback in the full implementation.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

package com.lifelog.feature.meds

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
fun MedsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Meds",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(modifier = Modifier.padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("List today's medications with dosage and time of day.", style = MaterialTheme.typography.bodyMedium)
                Text("Tapping a med should toggle taken/not taken and schedule reminders via WorkManager.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Full CRUD for medications belongs here.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

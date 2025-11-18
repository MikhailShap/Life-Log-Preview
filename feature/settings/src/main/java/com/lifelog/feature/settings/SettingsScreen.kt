package com.lifelog.feature.settings

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
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(modifier = Modifier.padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Toggle light/dark/system theme and switch languages (EN/RU).", style = MaterialTheme.typography.bodyMedium)
                Text("Locale changes should refresh UI without restart in the final version.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add backup/export and import of local data, plus privacy toggles.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

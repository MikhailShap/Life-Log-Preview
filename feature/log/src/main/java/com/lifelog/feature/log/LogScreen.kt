package com.lifelog.feature.log

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
fun LogScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Log",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Daily entries will be listed here with filters and calendar view.", style = MaterialTheme.typography.bodyMedium)
                Text("Tapping an entry should open details for review and edit.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

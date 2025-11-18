package com.lifelog.feature.trends

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
fun TrendsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Trends",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Charts for mood, energy, anxiety, and sleep will be rendered here.", style = MaterialTheme.typography.bodyMedium)
                Text("Switch between week and month views to match the MVP requirements.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

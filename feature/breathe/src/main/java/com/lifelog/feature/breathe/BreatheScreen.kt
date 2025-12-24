package com.lifelog.feature.breathe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BreatheScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Breathe",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Card(
            modifier = Modifier.padding(bottom = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF23202E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Provide guided breathing protocols like box breathing and 4-7-8.", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Include timers and simple animations to guide the pace.", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF23202E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Sessions should run offline with no network required.", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

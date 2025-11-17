package com.lifelog.feature.trends

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(
    viewModel: TrendsViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trends") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Mood Over Time")
            if (entries.size > 1) {
                MoodChart(entries = entries.sortedBy { it.date })
            } else {
                Text("Not enough data to show a trend.")
            }
        }
    }
}

@Composable
fun MoodChart(entries: List<Entry>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxMood = 5f
            val path = Path()

            val stepX = size.width / (entries.size - 1)
            val stepY = size.height / maxMood

            entries.forEachIndexed { index, entry ->
                val x = index * stepX
                val y = size.height - (entry.mood * stepY)
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
                drawCircle(Color.Blue, radius = 8f, center = Offset(x, y))
            }

            drawPath(
                path = path,
                color = Color.Blue,
                style = Stroke(width = 4f)
            )
        }
    }
}

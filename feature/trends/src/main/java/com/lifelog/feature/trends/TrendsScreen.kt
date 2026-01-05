package com.lifelog.feature.trends

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.Mood
import com.lifelog.core.domain.model.Sleep
import com.lifelog.core.ui.R
import com.lifelog.core.ui.components.ScreenHeader

@Composable
fun TrendsScreen(
    viewModel: TrendsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(id = R.string.trends_title),
            isDateSelector = false,
            actions = {
                TimeRangeSelector(
                    selectedRange = uiState.timeRange,
                    onRangeSelected = { viewModel.setTimeRange(it) },
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // Summary Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = stringResource(id = R.string.avg_mood),
                    value = uiState.averageMood,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = stringResource(id = R.string.avg_sleep),
                    value = uiState.averageSleep,
                    modifier = Modifier.weight(1f)
                )
            }

            // Mood Chart
            StatsCard(title = stringResource(id = R.string.mood_dynamics)) {
                if (uiState.moodData.isNotEmpty()) {
                    MoodChart(data = uiState.moodData)
                } else {
                    EmptyState(stringResource(id = R.string.no_mood_data))
                }
            }

            // Sleep Chart
            StatsCard(title = stringResource(id = R.string.sleep_duration_title)) {
                if (uiState.sleepData.isNotEmpty()) {
                    SleepChart(data = uiState.sleepData)
                } else {
                    EmptyState(stringResource(id = R.string.no_sleep_data))
                }
            }

            // Energy Bar Chart
            StatsCard(title = stringResource(id = R.string.energy_levels)) {
                if (uiState.moodData.isNotEmpty()) {
                    BarChart(
                        data = uiState.moodData.map { it.energy.toFloat() },
                        color = Color(0xFFFFB74D),
                        maxVal = 10f
                    )
                } else {
                    EmptyState(stringResource(id = R.string.no_energy_data))
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF23202E)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TimeRangeSelector(
    selectedRange: TimeRange, 
    onRangeSelected: (TimeRange) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        TimeRange.values().forEach { range ->
            val isSelected = range == selectedRange
            val rangeLabel = when(range) {
                TimeRange.WEEK -> stringResource(id = R.string.stats_week)
                TimeRange.MONTH -> stringResource(id = R.string.stats_month)
            }
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        RoundedCornerShape(6.dp)
                    )
                    .clickable { onRangeSelected(range) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = rangeLabel,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun StatsCard(title: String, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF23202E)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun EmptyState(msg: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(msg, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun MoodChart(data: List<Mood>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1).coerceAtLeast(1)
        val maxVal = 5f 

        val points = data.mapIndexed { index, mood ->
            val x = index * stepX
            val y = height - (mood.rating / maxVal) * height
            Offset(x, y)
        }

        if (points.size == 1) {
            drawCircle(color = primaryColor, center = points[0], radius = 6.dp.toPx())
            return@Canvas
        }

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val p1 = points[i]
                val p2 = points[i + 1]
                cubicTo(
                    (p1.x + p2.x) / 2, p1.y,
                    (p1.x + p2.x) / 2, p2.y,
                    p2.x, p2.y
                )
            }
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 4.dp.toPx())
        )
        
        val fillPath = Path().apply {
            addPath(path)
            lineTo(points.last().x, height)
            lineTo(points.first().x, height)
            close()
        }
        
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(primaryColor.copy(alpha = 0.5f), Color.Transparent)
            )
        )
    }
}

@Composable
fun SleepChart(data: List<Sleep>) {
    val color = Color(0xFF42A5F5)
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1).coerceAtLeast(1)
        
        val durations = data.map { 
            val diff = it.endTime - it.startTime
            (diff.toFloat() / (1000 * 60 * 60)) 
        }
        val maxVal = 12f 

        val points = durations.mapIndexed { index, hours ->
            val x = index * stepX
            val y = height - (hours.coerceAtMost(maxVal) / maxVal) * height
            Offset(x, y)
        }

        if (points.size == 1) {
             drawCircle(color = color, center = points[0], radius = 6.dp.toPx())
             return@Canvas
        }

        points.forEachIndexed { i, p ->
            if (i < points.size - 1) {
                drawLine(
                    color = color,
                    start = p,
                    end = points[i+1],
                    strokeWidth = 3.dp.toPx()
                )
            }
            drawCircle(color = color, center = p, radius = 4.dp.toPx())
        }
    }
}

@Composable
fun BarChart(data: List<Float>, color: Color, maxVal: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas
        
        val width = size.width
        val height = size.height
        val barWidth = (width / data.size) * 0.6f
        val stepX = width / data.size

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxVal) * height
            val x = index * stepX + (stepX - barWidth) / 2
            val y = height - barHeight
            
            drawRect(
                color = color,
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
        }
    }
}

package com.lifelog.feature.breathe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreatheScreen() {
    val size = remember { Animatable(100f) }

    LaunchedEffect(Unit) {
        size.animateTo(
            targetValue = 200f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Breathe") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Inhale... Exhale...")
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.Blue,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = Size(size.value * 2, size.value * 2),
                    style = Stroke(width = 10f)
                )
            }
        }
    }
}

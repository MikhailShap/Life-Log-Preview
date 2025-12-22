package com.lifelog.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Темно-фиолетовый градиент как на референсе
    // Верхний левый угол -> Нижний правый
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2E2A4A), // Глубокий фиолетовый сверху
            Color(0xFF1B1929), // Переход
            Color(0xFF121212)  // Почти черный снизу
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        content()
    }
}

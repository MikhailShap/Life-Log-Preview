package com.lifelog.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Modern date picker chip component with intuitive design.
 * Shows "Today", "Yesterday" for recent dates, or full date format.
 * Features a calendar icon and dropdown arrow to indicate it's clickable.
 */
@Composable
fun ModernDatePicker(
    selectedDate: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed)
            Color(0xFF3D3650)
        else
            Color(0xFF2D2940),
        animationSpec = tween(150),
        label = "bgColor"
    )

    val dateInfo = remember(selectedDate) {
        getDateDisplayInfo(selectedDate)
    }

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0xFF6C63FF).copy(alpha = 0.1f),
                spotColor = Color(0xFF6C63FF).copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.95f)
                    )
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Calendar icon with accent background
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6C63FF),
                                Color(0xFF5851DB)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Date text
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                // Relative label (Today/Yesterday) or day of week
                Text(
                    text = dateInfo.relativeLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ),
                    color = if (dateInfo.isToday)
                        Color(0xFF8B83FF)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                // Full date
                Text(
                    text = dateInfo.fullDate,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Dropdown arrow
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select date",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Compact version of the date picker for use in headers
 */
@Composable
fun CompactDatePicker(
    selectedDate: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "scale"
    )

    val dateInfo = remember(selectedDate) {
        getDateDisplayInfo(selectedDate)
    }

    Row(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2D2940).copy(alpha = 0.6f))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = Color(0xFF8B83FF),
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = if (dateInfo.isToday) dateInfo.relativeLabel else dateInfo.shortDate,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Select date",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(16.dp)
        )
    }
}

private data class DateDisplayInfo(
    val relativeLabel: String,
    val fullDate: String,
    val shortDate: String,
    val isToday: Boolean
)

private fun getDateDisplayInfo(timestamp: Long): DateDisplayInfo {
    val locale = Locale.getDefault()
    val selectedCal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val todayCal = Calendar.getInstance()

    val isToday = isSameDay(selectedCal, todayCal)

    val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = isSameDay(selectedCal, yesterdayCal)

    val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    val isTomorrow = isSameDay(selectedCal, tomorrowCal)

    val relativeLabel = when {
        isToday -> getLocalizedToday(locale)
        isYesterday -> getLocalizedYesterday(locale)
        isTomorrow -> getLocalizedTomorrow(locale)
        else -> SimpleDateFormat("EEEE", locale).format(Date(timestamp))
            .replaceFirstChar { it.uppercase() }
    }

    val fullDate = SimpleDateFormat("d MMMM", locale).format(Date(timestamp))
    val shortDate = SimpleDateFormat("d MMM", locale).format(Date(timestamp))

    return DateDisplayInfo(
        relativeLabel = relativeLabel,
        fullDate = fullDate,
        shortDate = shortDate,
        isToday = isToday
    )
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun getLocalizedToday(locale: Locale): String {
    return when (locale.language) {
        "ru" -> "Сегодня"
        "en" -> "Today"
        "de" -> "Heute"
        "fr" -> "Aujourd'hui"
        "es" -> "Hoy"
        else -> "Today"
    }
}

private fun getLocalizedYesterday(locale: Locale): String {
    return when (locale.language) {
        "ru" -> "Вчера"
        "en" -> "Yesterday"
        "de" -> "Gestern"
        "fr" -> "Hier"
        "es" -> "Ayer"
        else -> "Yesterday"
    }
}

private fun getLocalizedTomorrow(locale: Locale): String {
    return when (locale.language) {
        "ru" -> "Завтра"
        "en" -> "Tomorrow"
        "de" -> "Morgen"
        "fr" -> "Demain"
        "es" -> "Mañana"
        else -> "Tomorrow"
    }
}

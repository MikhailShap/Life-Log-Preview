package com.lifelog.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lifelog.core.ui.R
import com.lifelog.core.ui.theme.PrimaryPurple
import java.text.DateFormatSymbols
import java.util.*

@Composable
fun LifeLogDatePickerDialog(
    initialDate: Long = System.currentTimeMillis(),
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = remember { Calendar.getInstance().apply { timeInMillis = initialDate } }

    var selectedYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var displayYear by remember { mutableIntStateOf(selectedYear) }
    var displayMonth by remember { mutableIntStateOf(selectedMonth) }

    val locale = Locale.getDefault()
    val monthNames = remember(locale) {
        DateFormatSymbols(locale).months
    }

    // Day names based on locale
    val dayNames = remember(locale) {
        if (locale.language == "ru") {
            listOf("П", "В", "С", "Ч", "П", "С", "В")
        } else {
            listOf("M", "T", "W", "T", "F", "S", "S")
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B1929)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Month/Year header with navigation
                MonthYearHeader(
                    year = displayYear,
                    month = displayMonth,
                    monthNames = monthNames,
                    onPreviousMonth = {
                        if (displayMonth == 0) {
                            displayMonth = 11
                            displayYear -= 1
                        } else {
                            displayMonth -= 1
                        }
                    },
                    onNextMonth = {
                        if (displayMonth == 11) {
                            displayMonth = 0
                            displayYear += 1
                        } else {
                            displayMonth += 1
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Day names row
                DayNamesRow(dayNames = dayNames)

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar grid
                CalendarGrid(
                    year = displayYear,
                    month = displayMonth,
                    selectedDay = if (selectedYear == displayYear && selectedMonth == displayMonth) selectedDay else -1,
                    onDayClick = { day ->
                        selectedDay = day
                        selectedYear = displayYear
                        selectedMonth = displayMonth
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    PrimaryPurple.copy(alpha = 0.5f),
                                    PrimaryPurple.copy(alpha = 0.3f)
                                )
                            )
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White.copy(alpha = 0.8f)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel).uppercase(),
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                    }

                    // OK button
                    Button(
                        onClick = {
                            val resultCalendar = Calendar.getInstance()
                            resultCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)
                            resultCalendar.set(Calendar.MILLISECOND, 0)
                            onDateSelected(resultCalendar.timeInMillis)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok_button).uppercase(),
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthYearHeader(
    year: Int,
    month: Int,
    monthNames: Array<String>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month",
                tint = Color.White.copy(alpha = 0.7f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${monthNames[month].replaceFirstChar { it.uppercase() }} $year",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp)
            )
        }

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month",
                tint = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun DayNamesRow(dayNames: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dayNames.forEach { dayName ->
            Text(
                text = dayName,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    year: Int,
    month: Int,
    selectedDay: Int,
    onDayClick: (Int) -> Unit
) {
    val calendar = remember(year, month) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // Adjust for Monday as first day of week
    val startOffset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

    val totalCells = startOffset + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - startOffset + 1

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day in 1..daysInMonth) {
                            DayCell(
                                day = day,
                                isSelected = day == selectedDay,
                                onClick = { onDayClick(day) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryPurple else Color.Transparent,
        animationSpec = tween(200),
        label = "dayBackgroundColor"
    )

    val glowColor = PrimaryPurple.copy(alpha = 0.4f)

    Box(
        modifier = Modifier
            .size(40.dp)
            .then(
                if (isSelected) {
                    Modifier.drawBehind {
                        // Glow effect
                        drawCircle(
                            color = glowColor,
                            radius = size.minDimension / 2 + 4.dp.toPx()
                        )
                    }
                } else Modifier
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

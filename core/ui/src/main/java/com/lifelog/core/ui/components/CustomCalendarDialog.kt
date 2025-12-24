package com.lifelog.core.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CustomCalendarDialog(
    initialDate: Long,
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().apply { timeInMillis = initialDate }) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().apply { timeInMillis = initialDate }) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF151321),
                            Color(0xFF0E0E0E)
                        )
                    )
                )
                .systemBarsPadding()
        ) {
            // Decorative background glows
            Box(
                modifier = Modifier
                    .size(450.dp)
                    .offset(x = (-150).dp, y = (-120).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF3F376F).copy(alpha = 0.25f), Color.Transparent)
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 120.dp, y = 70.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF9189DF).copy(alpha = 0.2f), Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Month Year and Navigation
                CalendarHeader(
                    currentMonth = currentMonth,
                    onMonthChange = { currentMonth = it }
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Days of Week Header
                DaysOfWeekHeader()

                Spacer(modifier = Modifier.height(20.dp))

                // Calendar Grid with Month Animation
                AnimatedContent(
                    targetState = currentMonth,
                    transitionSpec = {
                        val direction = if (targetState.after(initialState)) 1 else -1
                        (slideInHorizontally { width -> direction * width } + fadeIn(tween(300)))
                            .togetherWith(slideOutHorizontally { width -> -direction * width } + fadeOut(tween(300)))
                    },
                    label = "month_anim"
                ) { month ->
                    CalendarGrid(
                        currentMonth = month,
                        selectedDate = selectedDate,
                        onDateClick = { selectedDate = it }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Modern Action Buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Button(
                        onClick = {
                            onDateSelected(selectedDate.timeInMillis)
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .clip(RoundedCornerShape(34.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF5D52A5), Color(0xFF3F376F))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "ПОДТВЕРДИТЬ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        "ОТМЕНА",
                        color = Color.White.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 1.sp,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    currentMonth: Calendar,
    onMonthChange: (Calendar) -> Unit
) {
    val locale = Locale("ru")
    // Use "LLLL" for standalone month name in nominative case (e.g., "Декабрь")
    val monthText = SimpleDateFormat("LLLL", locale).format(currentMonth.time).replaceFirstChar { it.uppercase() }
    val yearText = SimpleDateFormat("yyyy", locale).format(currentMonth.time)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val next = currentMonth.clone() as Calendar
                next.add(Calendar.MONTH, -1)
                onMonthChange(next)
            },
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft, 
                contentDescription = null, 
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(32.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = monthText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 32.sp
                )
            )
            Text(
                text = yearText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.3f),
                    letterSpacing = 6.sp,
                    fontSize = 16.sp
                )
            )
        }

        IconButton(
            onClick = {
                val next = currentMonth.clone() as Calendar
                next.add(Calendar.MONTH, 1)
                onMonthChange(next)
            },
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowRight, 
                contentDescription = null, 
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun DaysOfWeekHeader() {
    val days = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color(0xFF9189DF).copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: Calendar,
    selectedDate: Calendar,
    onDateClick: (Calendar) -> Unit
) {
    val days = remember(currentMonth) { getDaysOfMonth(currentMonth) }
    val today = Calendar.getInstance()

    Column(modifier = Modifier.fillMaxWidth()) {
        days.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    if (date != null) {
                        CalendarDay(
                            date = date,
                            isSelected = isSameDay(date, selectedDate),
                            isToday = isSameDay(date, today),
                            isCurrentMonth = date.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH),
                            onDateClick = onDateClick,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
                // If the last week has fewer than 7 days, fill it with spacers
                if (week.size < 7) {
                    repeat(7 - week.size) {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: Calendar,
    isSelected: Boolean,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    onDateClick: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isSelected -> Color.White
            isToday -> Color(0xFF9189DF)
            isCurrentMonth -> Color.White.copy(alpha = 0.8f)
            else -> Color.White.copy(alpha = 0.2f)
        },
        label = "color"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onDateClick(date) }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            // Soft glow background
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF9189DF).copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
            // Main selection circle
            Box(
                modifier = Modifier
                    .fillMaxSize(0.85f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF5D52A5), Color(0xFF3F376F))
                        ),
                        CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.5f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.get(Calendar.DAY_OF_MONTH).toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                    color = contentColor,
                    fontSize = 22.sp
                )
            )
            if (isToday && !isSelected) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color(0xFF9189DF), CircleShape)
                )
            }
        }
    }
}

private fun getDaysOfMonth(month: Calendar): List<Calendar?> {
    val calendar = month.clone() as Calendar
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val offset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - Calendar.MONDAY
    
    val days = mutableListOf<Calendar?>()
    repeat(offset) { days.add(null) }
    
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (i in 1..daysInMonth) {
        val day = calendar.clone() as Calendar
        day.set(Calendar.DAY_OF_MONTH, i)
        days.add(day)
    }
    return days
}

private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
           c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
           c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
}

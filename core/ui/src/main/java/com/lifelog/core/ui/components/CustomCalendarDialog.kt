package com.lifelog.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF151321) // Dark background matching the image
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF23203B),
                                Color(0xFF151321)
                            )
                        )
                    )
                    .systemBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header: Month Year and Navigation
                    CalendarHeader(
                        currentMonth = currentMonth,
                        onMonthChange = { currentMonth = it }
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Days of Week Header
                    DaysOfWeekHeader()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Calendar Grid
                    CalendarGrid(
                        currentMonth = currentMonth,
                        selectedDate = selectedDate,
                        onDateClick = { selectedDate = it }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Footer: Cancel and OK Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismissRequest,
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                            shape = RoundedCornerShape(32.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3F376F)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF9189DF))
                        ) {
                            Text("ОТМЕНА", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        }

                        Button(
                            onClick = {
                                onDateSelected(selectedDate.timeInMillis)
                                onDismissRequest()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                            shape = RoundedCornerShape(32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3F376F),
                                contentColor = Color.White
                            )
                        ) {
                            Text("OK", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
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
    val locale = Locale.getDefault()
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", locale)
    val monthYearText = monthYearFormat.format(currentMonth.time).replaceFirstChar { it.uppercase() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val newMonth = currentMonth.clone() as Calendar
            newMonth.add(Calendar.MONTH, -1)
            onMonthChange(newMonth)
        }) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev", tint = Color(0xFF9189DF), modifier = Modifier.size(32.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* Could add month picker later */ }
        ) {
            Text(
                text = monthYearText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(start = 4.dp).size(28.dp)
            )
        }

        IconButton(onClick = {
            val newMonth = currentMonth.clone() as Calendar
            newMonth.add(Calendar.MONTH, 1)
            onMonthChange(newMonth)
        }) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next", tint = Color(0xFF9189DF), modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
private fun DaysOfWeekHeader() {
    val days = listOf("П", "В", "С", "Ч", "П", "С", "В")
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF9189DF),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
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

    Column(modifier = Modifier.fillMaxWidth()) {
        val chunkedDays = days.chunked(7)
        chunkedDays.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                week.forEach { date ->
                    if (date != null) {
                        val isSelected = isSameDay(date, selectedDate)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .then(
                                    if (isSelected) {
                                        Modifier.background(
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0xFF9189DF).copy(alpha = 0.5f),
                                                    Color(0xFF9189DF).copy(alpha = 0.2f),
                                                    Color.Transparent
                                                )
                                            )
                                        ).background(Color(0xFF3F376F).copy(alpha = 0.8f), CircleShape)
                                    } else Modifier
                                )
                                .clickable { onDateClick(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.get(Calendar.DAY_OF_MONTH).toString(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 20.sp
                                )
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
                if (week.size < 7) {
                    repeat(7 - week.size) {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
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
    for (i in 0 until offset) {
        days.add(null)
    }
    
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

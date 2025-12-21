package com.lifelog.feature.log

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifelog.core.ui.R
import com.lifelog.feature.meds.MedsScreen
import com.lifelog.feature.sideeffects.SideEffectsScreen
import com.lifelog.feature.today.TodayScreen
import com.lifelog.feature.videonotes.VideoNotesScreen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class LogSubScreen(val labelRes: Int, val icon: ImageVector) {
    MOOD(R.string.menu_mood, Icons.Default.SentimentSatisfied),
    SLEEP(R.string.menu_sleep, Icons.Default.Bedtime),
    MEDS(R.string.menu_meds, Icons.Default.Medication),
    SIDE_EFFECTS(R.string.menu_side_effects, Icons.Default.Healing),
    VIDEO_NOTE(R.string.menu_video_note, Icons.Default.Videocam)
}

@Composable
fun LogRootScreen(
    onNavigateToRecord: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(LogSubScreen.MOOD) }
    
    // Global date state for all log screens
    var selectedDateInMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateInMillis }
    
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val newCalendar = Calendar.getInstance()
            newCalendar.set(year, month, dayOfMonth)
            selectedDateInMillis = newCalendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    HorizontalDivider()
                    LogSubScreen.values().forEach { screen ->
                        NavigationDrawerItem(
                            label = { Text(stringResource(screen.labelRes)) },
                            icon = { Icon(screen.icon, contentDescription = null) },
                            selected = currentScreen == screen,
                            onClick = {
                                currentScreen = screen
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
        content = {
            val onMenuClick: () -> Unit = {
                scope.launch { drawerState.open() }
            }
            
            val onDateClick: () -> Unit = {
                datePickerDialog.show()
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    LogSubScreen.MOOD -> LogScreen(
                        onNavigateToRecord = onNavigateToRecord, 
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDateInMillis,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.SLEEP -> TodayScreen(
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDateInMillis,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.MEDS -> MedsScreen(
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDateInMillis,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.SIDE_EFFECTS -> SideEffectsScreen(
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDateInMillis,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.VIDEO_NOTE -> VideoNotesScreen(
                        onNavigateToRecord = onNavigateToRecord, 
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDateInMillis,
                        onDateClick = onDateClick
                    )
                }
            }
        }
    )
}

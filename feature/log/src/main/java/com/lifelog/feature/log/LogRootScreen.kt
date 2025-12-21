package com.lifelog.feature.log

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifelog.core.ui.R
import com.lifelog.feature.meds.MedsScreen
import com.lifelog.feature.sideeffects.SideEffectsScreen
import com.lifelog.feature.today.TodayScreen
import com.lifelog.feature.videonotes.VideoNotesScreen
import kotlinx.coroutines.launch

enum class LogSubScreen(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    MOOD(R.string.menu_mood, Icons.Default.Face),
    SLEEP(R.string.menu_sleep, Icons.Default.Bedtime),
    MEDS(R.string.menu_meds, Icons.Default.Medication),
    SIDE_EFFECTS(R.string.menu_side_effects, Icons.Default.Warning),
    VIDEO_NOTE(R.string.menu_video_note, Icons.Default.Videocam)
}

@Composable
fun LogRootScreen(
    currentSubScreenName: String = "MOOD",
    onSubScreenChange: (String) -> Unit = {},
    onNavigateToRecord: () -> Unit = {},
    selectedDate: Long,
    onDateClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val currentScreen = try {
        LogSubScreen.valueOf(currentSubScreenName)
    } catch (e: Exception) {
        LogSubScreen.MOOD
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    HorizontalDivider()
                    LogSubScreen.entries.forEach { screen ->
                        NavigationDrawerItem(
                            label = { Text(stringResource(screen.labelRes)) },
                            icon = { Icon(screen.icon, contentDescription = null) },
                            selected = currentScreen == screen,
                            onClick = {
                                onSubScreenChange(screen.name)
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

            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    LogSubScreen.MOOD -> LogScreen(
                        onNavigateToRecord = onNavigateToRecord, 
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDate,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.SLEEP -> TodayScreen(
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDate,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.MEDS -> MedsScreen(
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDate,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.SIDE_EFFECTS -> SideEffectsScreen(
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDate,
                        onDateClick = onDateClick
                    )
                    LogSubScreen.VIDEO_NOTE -> VideoNotesScreen(
                        onNavigateToRecord = onNavigateToRecord, 
                        onMenuClick = onMenuClick,
                        selectedDate = selectedDate,
                        onDateClick = onDateClick
                    )
                }
            }
        }
    )
}

package com.lifelog.feature.log

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lifelog.core.ui.R
import com.lifelog.feature.meds.MedsScreen
import com.lifelog.feature.sideeffects.SideEffectsScreen
import com.lifelog.feature.today.TodayScreen
import com.lifelog.feature.videonotes.VideoNotesScreen
import kotlinx.coroutines.launch

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                // Обнуляем инсеты, чтобы фон шторки был от самого верха экрана
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                // Добавляем отступ для контента, чтобы он не перекрывался статус-баром
                Spacer(Modifier.statusBarsPadding())
                
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
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
        },
        content = {
            val onMenuClick: () -> Unit = {
                scope.launch { drawerState.open() }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    LogSubScreen.MOOD -> LogScreen(onNavigateToRecord = onNavigateToRecord, onMenuClick = onMenuClick)
                    LogSubScreen.SLEEP -> TodayScreen(onMenuClick = onMenuClick)
                    LogSubScreen.MEDS -> MedsScreen(onMenuClick = onMenuClick)
                    LogSubScreen.SIDE_EFFECTS -> SideEffectsScreen(onMenuClick = onMenuClick)
                    LogSubScreen.VIDEO_NOTE -> VideoNotesScreen(onNavigateToRecord = onNavigateToRecord, onMenuClick = onMenuClick)
                }
            }
        }
    )
}

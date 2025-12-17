package com.lifelog.feature.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp // Added import
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.ui.R
import com.lifelog.feature.today.TodayScreen
import com.lifelog.feature.videonotes.RecordVideoScreen
import com.lifelog.feature.videonotes.VideoNotesViewModel
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
    onNavigateToRecord: () -> Unit = {} // Keep for now if needed inside Mood
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(LogSubScreen.MOOD) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
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
    ) {
        // Content Wrapper
        // We pass the onMenuClick to sub-screens so they can open the drawer
        val onMenuClick: () -> Unit = {
            scope.launch { drawerState.open() }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                LogSubScreen.MOOD -> {
                    // Reusing the existing LogScreen composable for Mood
                    // We need to modify LogScreen to accept onMenuClick
                    LogScreen(
                        onNavigateToRecord = onNavigateToRecord,
                        onMenuClick = onMenuClick
                    )
                }
                LogSubScreen.SLEEP -> {
                    TodayScreen(
                        onMenuClick = onMenuClick
                    )
                }
                LogSubScreen.MEDS -> {
                    PlaceholderScreen("Meds", onMenuClick)
                }
                LogSubScreen.SIDE_EFFECTS -> {
                    PlaceholderScreen("Side Effects", onMenuClick)
                }
                LogSubScreen.VIDEO_NOTE -> {
                    // Video Note as a screen in drawer, or action?
                    // TS says: "Видео заметка" in menu.
                    // If it's a screen list:
                    PlaceholderScreen("Video Notes List", onMenuClick)
                    // If it's direct record, we might show the recorder directly or navigate
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(title: String, onMenuClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "$title Screen", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

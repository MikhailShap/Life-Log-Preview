package com.lifelog.feature.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

enum class LogSubScreen(val labelRes: Int, val icon: ImageVector) {
    MOOD(R.string.menu_mood, Icons.Default.SentimentSatisfied),
    SLEEP(R.string.menu_sleep, Icons.Default.Bedtime),
    MEDS(R.string.menu_meds, Icons.Default.Medication),
    SIDE_EFFECTS(R.string.menu_side_effects, Icons.Default.Healing),
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
    
    val currentScreen = remember(currentSubScreenName) {
        try {
            LogSubScreen.valueOf(currentSubScreenName)
        } catch (e: Exception) {
            LogSubScreen.MOOD
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                windowInsets = WindowInsets(0, 0, 0, 0),
                drawerContainerColor = Color.Transparent, // Прозрачный, чтобы был виден градиент
                drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
                modifier = Modifier.width(280.dp).fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2E2A4A), // Глубокий фиолетовый сверху
                                    Color(0xFF1B1929), // Переход
                                    Color(0xFF121212)  // Почти черный снизу
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = "LifeLog",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(start = 16.dp, bottom = 32.dp)
                        )
                        
                        LogSubScreen.values().forEach { screen ->
                            val isSelected = currentScreen == screen
                            NavigationDrawerItem(
                                label = { 
                                    Text(
                                        stringResource(screen.labelRes),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                        )
                                    ) 
                                },
                                icon = { 
                                    Icon(
                                        screen.icon, 
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    ) 
                                },
                                selected = isSelected,
                                onClick = {
                                    onSubScreenChange(screen.name)
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .height(56.dp),
                                shape = RoundedCornerShape(28.dp),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    unselectedContainerColor = Color.Transparent,
                                    selectedIconColor = Color.White,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    selectedTextColor = Color.White,
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f)
                                )
                            )
                        }
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

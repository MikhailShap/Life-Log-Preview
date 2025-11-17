package com.lifelog.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.ui.theme.LifeLogAppTheme
import com.lifelog.feature.breathe.BreatheScreen
import com.lifelog.feature.library.LibraryScreen
import com.lifelog.feature.log.LogScreen
import com.lifelog.feature.meds.MedsScreen
import com.lifelog.feature.settings.SettingsScreen
import com.lifelog.feature.today.TodayScreen
import com.lifelog.feature.trends.TrendsScreen
import com.lifelog.feature.videonotes.RecordVideoScreen
import com.lifelog.feature.videonotes.VideoNotesScreen
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Today : Screen("today", "Today", Icons.Default.Edit)
    object Log : Screen("log", "Log", Icons.Default.DateRange)
    object Trends : Screen("trends", "Trends", Icons.Default.Star)
    object VideoNotes : Screen("video_notes", "Videos", Icons.Default.Videocam)
    object Meds : Screen("meds", "Meds", Icons.Default.Favorite)
    object Breathe : Screen("breathe", "Breathe", Icons.Default.WbSunny)
    object Library : Screen("library", "Library", Icons.AutoMirrored.Filled.MenuBook)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

const val recordVideoRoute = "record_video"

val items = listOf(
    Screen.Today,
    Screen.Log,
    Screen.Trends,
    Screen.VideoNotes,
    Screen.Meds,
    Screen.Breathe,
    Screen.Library,
    Screen.Settings
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            LifeLogAppTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    label = { Text(screen.label) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(navController, startDestination = Screen.Today.route, Modifier.padding(innerPadding)) {
                        composable(Screen.Today.route) { TodayScreen() }
                        composable(Screen.Log.route) { LogScreen() }
                        composable(Screen.Trends.route) { TrendsScreen() }
                        composable(Screen.VideoNotes.route) {
                            VideoNotesScreen(
                                onAddVideo = { navController.navigate(recordVideoRoute) }
                            )
                        }
                        composable(recordVideoRoute) {
                            RecordVideoScreen(
                                onVideoSaved = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.Meds.route) { MedsScreen() }
                        composable(Screen.Breathe.route) { BreatheScreen() }
                        composable(Screen.Library.route) { LibraryScreen() }
                        composable(Screen.Settings.route) { SettingsScreen() }
                    }
                }
            }
        }
    }
}

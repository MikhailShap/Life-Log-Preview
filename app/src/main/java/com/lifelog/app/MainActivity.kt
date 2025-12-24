package com.lifelog.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifelog.core.domain.model.ThemeMode
import com.lifelog.core.ui.theme.LifeLogAppTheme
import com.lifelog.core.ui.components.AppBackground
import com.lifelog.core.ui.components.CustomCalendarDialog
import com.lifelog.feature.log.LogRootScreen
import com.lifelog.feature.settings.SettingsScreen
import com.lifelog.feature.trends.TrendsScreen
import dagger.hilt.android.AndroidEntryPoint
import com.lifelog.core.ui.R
import com.lifelog.feature.videonotes.RecordVideoScreen
import com.lifelog.feature.videonotes.VideoNotesViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector) {
    object Log : Screen("log", R.string.nav_log, Icons.Default.RadioButtonChecked)
    object Stats : Screen("stats", R.string.nav_stats, Icons.Default.BarChart)
    object Profile : Screen("profile", R.string.nav_profile, Icons.Default.Person)
}

val items = listOf(
    Screen.Log,
    Screen.Stats,
    Screen.Profile,
)

const val recordVideoRoute = "record_video"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            
            val logSubScreenName by viewModel.currentLogSubScreen.collectAsState()

            LifeLogAppTheme(darkTheme = darkTheme) {
                var selectedDateInMillis by remember { mutableStateOf(System.currentTimeMillis()) }
                var showDatePicker by remember { mutableStateOf(false) }

                if (showDatePicker) {
                    CustomCalendarDialog(
                        initialDate = selectedDateInMillis,
                        onDismissRequest = { showDatePicker = false },
                        onDateSelected = { date ->
                            selectedDateInMillis = date
                            showDatePicker = false
                        }
                    )
                }

                AppBackground {
                    val navController = rememberNavController()
                    
                    Scaffold(
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        bottomBar = {
                            NavigationBar(
                                containerColor = Color(0xFF151321).copy(alpha = 0.95f)
                            ) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(stringResource(screen.labelRes)) },
                                        selected = selected,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.White,
                                            selectedTextColor = Color.White,
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                                        )
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Log.route,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                composable(Screen.Log.route) {
                                    LogRootScreen(
                                        currentSubScreenName = logSubScreenName,
                                        onSubScreenChange = { viewModel.setLogSubScreen(it) },
                                        onNavigateToRecord = { navController.navigate(recordVideoRoute) },
                                        selectedDate = selectedDateInMillis,
                                        onDateClick = { showDatePicker = true }
                                    )
                                }
                                composable(Screen.Stats.route) { 
                                    TrendsScreen() 
                                }
                                composable(Screen.Profile.route) { SettingsScreen() }
                                composable(recordVideoRoute) {
                                    val videoViewModel: VideoNotesViewModel = hiltViewModel()
                                    RecordVideoScreen(
                                        viewModel = videoViewModel,
                                        onVideoSaved = { 
                                            navController.popBackStack() 
                                        },
                                        onBack = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

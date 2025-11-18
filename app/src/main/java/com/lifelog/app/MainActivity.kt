package com.lifelog.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
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
import com.lifelog.feature.settings.SettingsScreen
import com.lifelog.feature.today.TodayScreen
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Sleep : Screen("sleep", "Сон", Icons.Default.Bedtime)
    object Mood : Screen("mood", "Настроение", Icons.Default.SentimentSatisfied)
    object Stats : Screen("stats", "Статистика", Icons.Default.BarChart)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
}

val items = listOf(
    Screen.Sleep,
    Screen.Mood,
    Screen.Stats,
    Screen.Profile,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: Activity starting")
        super.onCreate(savedInstanceState)
        setContent {
            Log.d(TAG, "setContent: Composable content being set")
            val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            LifeLogAppTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                Log.d(TAG, "onCreate: NavController created")
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
                                        Log.d(TAG, "Navigation: Navigating to ${screen.route}")
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
                    NavHost(navController, startDestination = Screen.Sleep.route, Modifier.padding(innerPadding)) {
                        composable(Screen.Sleep.route) { TodayScreen() }
                        composable(Screen.Mood.route) { Text("Mood Screen") } // Placeholder
                        composable(Screen.Stats.route) { Text("Stats Screen") } // Placeholder
                        composable(Screen.Profile.route) { SettingsScreen() }
                    }
                }
            }
        }
        Log.d(TAG, "onCreate: Activity creation finished")
    }
}

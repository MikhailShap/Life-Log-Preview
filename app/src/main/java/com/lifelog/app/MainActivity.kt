package com.lifelog.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.lifelog.core.ui.R
import java.util.Locale

private const val TAG = "MainActivity"

sealed class Screen(val route: String, val label: Int, val icon: ImageVector) {
    object Sleep : Screen("sleep", R.string.nav_sleep, Icons.Default.Bedtime)
    object Mood : Screen("mood", R.string.nav_mood, Icons.Default.SentimentSatisfied)
    object Stats : Screen("stats", R.string.nav_stats, Icons.Default.BarChart)
    object Profile : Screen("profile", R.string.nav_profile, Icons.Default.Person)
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
        
        viewModel.language.onEach { lang ->
            val appLocale = LocaleListCompat.forLanguageTags(lang)
            if (AppCompatDelegate.getApplicationLocales() != appLocale) {
                AppCompatDelegate.setApplicationLocales(appLocale)
                recreate()
            }
        }.launchIn(lifecycleScope)

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
                                    label = { Text(stringResource(screen.label)) },
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
                    NavHost(navController, startDestination = Screen.Sleep.route, Modifier.padding(innerPadding)) {
                        composable(Screen.Sleep.route) { TodayScreen() }
                        composable(Screen.Mood.route) { Text("Mood Screen") }
                        composable(Screen.Stats.route) { Text("Stats Screen") }
                        composable(Screen.Profile.route) { SettingsScreen() }
                    }
                }
            }
        }
        Log.d(TAG, "onCreate: Activity creation finished")
    }
}

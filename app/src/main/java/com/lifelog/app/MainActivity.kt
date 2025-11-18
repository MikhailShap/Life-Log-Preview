package com.lifelog.app

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
import androidx.compose.ui.platform.LocalConfiguration
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
import com.lifelog.feature.log.LogScreen
import com.lifelog.feature.settings.SettingsScreen
import com.lifelog.feature.today.TodayScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.lifelog.core.ui.R
import java.util.Locale
import com.lifelog.feature.videonotes.RecordVideoScreen
import com.lifelog.feature.videonotes.VideoNotesScreen
import com.lifelog.feature.videonotes.VideoNotesViewModel
import androidx.hilt.navigation.compose.hiltViewModel

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

const val recordVideoRoute = "record_video"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        
        // Keep AppCompatDelegate sync for system compatibility
        viewModel.language
            .distinctUntilChanged()
            .onEach { language ->
                val appLocale = LocaleListCompat.forLanguageTags(language)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
            .launchIn(lifecycleScope)

        setContent {
            val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val language by viewModel.language.collectAsState(initial = "en")
            
            // Force locale update for Compose
            val locale = Locale(language)
            val configuration = LocalConfiguration.current
            configuration.setLocale(locale)
            val resources = LocalContext.current.resources
            resources.updateConfiguration(configuration, resources.displayMetrics)

            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            LifeLogAppTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    label = { Text(stringResource(screen.label)) },
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
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(navController, startDestination = Screen.Sleep.route, Modifier.padding(innerPadding)) {
                        composable(Screen.Sleep.route) { TodayScreen() }
                        composable(Screen.Mood.route) {
                            LogScreen(
                                onNavigateToRecord = { navController.navigate(recordVideoRoute) }
                            )
                        }
                        composable(Screen.Stats.route) { Text("Stats Screen") }
                        composable(Screen.Profile.route) { SettingsScreen() }
                        composable(recordVideoRoute) {
                            val viewModel: VideoNotesViewModel = hiltViewModel()
                            RecordVideoScreen(
                                viewModel = viewModel,
                                onVideoSaved = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

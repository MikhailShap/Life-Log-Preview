package com.lifelog.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MovieCreation
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifelog.core.ui.theme.LifeLogAppTheme
import com.lifelog.feature.breathe.BreatheScreen
import com.lifelog.feature.library.LibraryScreen
import com.lifelog.feature.log.LogScreen
import com.lifelog.feature.meds.MedsScreen
import com.lifelog.feature.settings.SettingsScreen
import com.lifelog.feature.today.TodayScreen
import com.lifelog.feature.trends.TrendsScreen
import com.lifelog.feature.videonotes.VideoNotesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeLogAppTheme {
                LifeLogApp()
            }
        }
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
private fun LifeLogApp() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem(
            route = "today",
            label = "Today",
            icon = { Icon(Icons.Outlined.Today, contentDescription = "Today") }
        ),
        BottomNavItem(
            route = "log",
            label = "Log",
            icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = "Log") }
        ),
        BottomNavItem(
            route = "trends",
            label = "Trends",
            icon = { Icon(Icons.Outlined.BarChart, contentDescription = "Trends") }
        ),
        BottomNavItem(
            route = "meds",
            label = "Meds",
            icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Meds") }
        ),
        BottomNavItem(
            route = "videos",
            label = "Videos",
            icon = { Icon(Icons.Outlined.MovieCreation, contentDescription = "Video Notes") }
        ),
        BottomNavItem(
            route = "breathe",
            label = "Breathe",
            icon = { Icon(Icons.Outlined.Bolt, contentDescription = "Breathe") }
        ),
        BottomNavItem(
            route = "library",
            label = "Library",
            icon = { Icon(Icons.Outlined.Article, contentDescription = "Library") }
        ),
        BottomNavItem(
            route = "settings",
            label = "Settings",
            icon = { Icon(Icons.Outlined.Settings, contentDescription = "Settings") }
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = items,
                currentDestination = navController.currentBackStackEntryAsState().value?.destination
            ) { selected ->
                navController.navigate(selected.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = items.first().route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("today") { TodayScreen() }
            composable("log") { LogScreen() }
            composable("trends") { TrendsScreen() }
            composable("meds") { MedsScreen() }
            composable("videos") { VideoNotesScreen() }
            composable("breathe") { BreatheScreen() }
            composable("library") { LibraryScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        val currentRoute = currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemSelected(item) },
                icon = { item.icon() },
                label = { Text(item.label) }
            )
        }
    }
}

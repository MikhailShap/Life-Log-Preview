package com.lifelog.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lifelog.feature.log.LogScreen
import com.lifelog.feature.today.TodayScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "today") {
        composable("today") { TodayScreen() }
        composable("log") { LogScreen() }
    }
}

package com.joao.otavio.kaizen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joao.otavio.kaizen.navigation.Route.EVENTS_SCREEN

@Composable
fun KaizenNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = EVENTS_SCREEN
    ) {
        composable(EVENTS_SCREEN) {

        }
    }
}

object Route {
    const val EVENTS_SCREEN = "events"
}
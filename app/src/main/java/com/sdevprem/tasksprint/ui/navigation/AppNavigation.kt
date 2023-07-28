package com.sdevprem.tasksprint.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sdevprem.tasksprint.ui.auth.AuthScreen
import com.sdevprem.tasksprint.ui.edit_task.EditTaskScreen
import com.sdevprem.tasksprint.ui.home.HomeScreen

@Composable
fun AppNavigation(
    navHostController: NavHostController,
    isUserSignedIn: Boolean
) {
    NavHost(
        navController = navHostController,
        startDestination =
        if (isUserSignedIn)
            Destination.HomeScreen.route
        else Destination.AuthScreen.route
    ) {
        composable(route = Destination.AuthScreen.route) {
            AuthScreen(navController = navHostController)
        }

        composable(route = Destination.HomeScreen.route) {
            HomeScreen(navController = navHostController)
        }

        composable(
            route = Destination.EditTaskScreen.route,
            arguments = Destination.EditTaskScreen.getArgumentList()
        ) {
            EditTaskScreen(navController = navHostController)
        }
    }

}
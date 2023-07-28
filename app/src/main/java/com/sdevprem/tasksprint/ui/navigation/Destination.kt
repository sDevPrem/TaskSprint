package com.sdevprem.tasksprint.ui.navigation

import androidx.navigation.navArgument

sealed class Destination(val route: String) {
    object AuthScreen : Destination("auth_screen_route")
    object HomeScreen : Destination("home_screen_route")
    object EditTaskScreen : Destination(EDIT_TASK_SCREEN_ROUTE) {
        const val TASK_ID_KEY = "task_id"

        fun getArgumentList() = listOf(
            navArgument(name = TASK_ID_KEY) {
                this.nullable = true
                this.defaultValue = null
            }
        )

        fun passArgument(
            taskId: String? = null
        ): String {
            return route
                .run {
                    taskId?.let {
                        replace("{$TASK_ID_KEY}", it)
                    } ?: replace(TASK_ID_KEY, "")
                }
        }
    }

    companion object {
        private const val EDIT_TASK_SCREEN_ROUTE =
            "edit_task_screen_route?${EditTaskScreen.TASK_ID_KEY}={${EditTaskScreen.TASK_ID_KEY}}"
    }
}
package com.sdevprem.tasksprint.ui.home

import com.sdevprem.tasksprint.core.model.Task
import java.util.Date

data class HomeUIState(
    val taskList: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val date: Date = Date()
)

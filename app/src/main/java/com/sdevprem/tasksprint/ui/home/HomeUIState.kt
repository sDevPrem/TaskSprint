package com.sdevprem.tasksprint.ui.home

import com.sdevprem.tasksprint.data.model.Task
import java.util.Date

data class HomeUIState(
    val taskList: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val date: Date = Date()
)

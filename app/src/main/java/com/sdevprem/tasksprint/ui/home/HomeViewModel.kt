package com.sdevprem.tasksprint.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdevprem.tasksprint.data.model.Task
import com.sdevprem.tasksprint.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val tasks = taskRepository.tasks

    fun onTaskCheckChange(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(completed = !task.completed))
        }
    }

}
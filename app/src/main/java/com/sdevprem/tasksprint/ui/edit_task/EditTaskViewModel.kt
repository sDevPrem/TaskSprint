package com.sdevprem.tasksprint.ui.edit_task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdevprem.tasksprint.data.model.Task
import com.sdevprem.tasksprint.data.repository.TaskRepository
import com.sdevprem.tasksprint.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), EditTaskEvents {
    val task = mutableStateOf(Task())

    init {
        savedStateHandle.get<String>(Destination.EditTaskScreen.TASK_ID_KEY)?.let {
            viewModelScope.launch {
                task.value =
                    repository.getTask(it)
                        ?: Task()
            }
        }
    }

    override fun onTitleChange(newValue: String) {
        task.value = task.value.copy(title = newValue)
    }

    override fun onDescriptionChange(newValue: String) {
        task.value = task.value.copy(description = newValue)
    }

    override fun onDateChange(newValue: Date) {
        task.value = task.value.copy(dueDate = newValue)
    }

    override fun onDoneClick(popUpScreen: () -> Unit) {
        viewModelScope.launch {
            val editTask = task.value
            if (editTask.id.isBlank())
                repository.saveTask(editTask)
            else repository.updateTask(editTask)
            popUpScreen()
        }
    }

    override fun onDeleteClick(popUpScreen: () -> Unit) {
        viewModelScope.launch {
            if (task.value.id.isNotBlank())
                repository.deleteTask(task.value.id)
            popUpScreen()
        }
    }
}

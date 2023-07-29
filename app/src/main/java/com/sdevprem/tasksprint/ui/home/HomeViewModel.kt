package com.sdevprem.tasksprint.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdevprem.tasksprint.common.ext.setDateToFirstDateOfMonth
import com.sdevprem.tasksprint.common.ext.setDateToLastDateOfMonth
import com.sdevprem.tasksprint.common.ext.toCalendar
import com.sdevprem.tasksprint.core.auth.PhoneAuthHandler
import com.sdevprem.tasksprint.core.model.Task
import com.sdevprem.tasksprint.core.repository.TaskRepository
import com.sdevprem.tasksprint.core.utils.TaskFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val authHandler: PhoneAuthHandler
) : ViewModel() {

    private val taskFilter = MutableStateFlow(
        TaskFilter(
            fromDate = Calendar.getInstance().setDateToFirstDateOfMonth().time,
            toDate = Calendar.getInstance().setDateToLastDateOfMonth().time
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasks = taskFilter.flatMapLatest { filter ->
        _homeUiState.update {
            it.copy(taskList = emptyList(), isLoading = true, date = filter.fromDate)
        }
        taskRepository.getFilteredTask(filter)
    }

    private val _homeUiState = MutableStateFlow(HomeUIState())
    val homeUiState = _homeUiState.asStateFlow()

    init {
        tasks.onEach { list ->
            _homeUiState.update { it.copy(taskList = list, isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun onTaskCheckChange(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(completed = !task.completed))
        }
    }

    fun onLogOutClick() = authHandler.logout()


    fun onIncrementDate() {
        taskFilter.update {
            it.copy(
                fromDate = it.fromDate.toCalendar().apply { add(Calendar.MONTH, 1) }
                    .setDateToFirstDateOfMonth().time,
                toDate = it.fromDate.toCalendar().apply { add(Calendar.MONTH, 1) }
                    .setDateToLastDateOfMonth().time
            )
        }
    }

    fun onDecrementDate() {
        taskFilter.update {
            it.copy(
                fromDate = it.fromDate.toCalendar().apply { add(Calendar.MONTH, -1) }
                    .setDateToFirstDateOfMonth().time,
                toDate = it.fromDate.toCalendar().apply { add(Calendar.MONTH, -1) }
                    .setDateToLastDateOfMonth().time
            )
        }
    }

}
package com.sdevprem.tasksprint.ui.edit_task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.sdevprem.tasksprint.R
import com.sdevprem.tasksprint.common.ext.getDisplayDueDate
import com.sdevprem.tasksprint.common.ext.toDate
import com.sdevprem.tasksprint.data.model.Task
import java.util.Date

@Composable
fun EditTaskScreen(
    navController: NavController,
    viewModel: EditTaskViewModel = hiltViewModel()
) {
    val task by viewModel.task
    EditTaskScreenContent(
        task = task,
        editTaskEvents = viewModel,
        popUpScreen = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskScreenContent(
    task: Task,
    editTaskEvents: EditTaskEvents,
    popUpScreen: () -> Unit
) {
    val calendarState = rememberUseCaseState()
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date {
            editTaskEvents.onDateChange(
                it.toDate()
            )
        },
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = if (task.id.isNotBlank()) "Edit Task" else "Add Task"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(onClick = {
                    editTaskEvents.onDoneClick(popUpScreen)
                }) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Save or Update task",
                    )
                }
                if (task.id.isNotBlank())
                    IconButton(onClick = {
                        editTaskEvents.onDeleteClick(popUpScreen)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete task",
                        )
                    }
            },
            navigationIcon = {
                IconButton(onClick = popUpScreen) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_toward_left),
                        contentDescription = "Navigate back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = task.title,
                onValueChange = editTaskEvents::onTitleChange,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                placeholder = {
                    Text(text = "Title")
                }
            )

            OutlinedTextField(
                value = task.description,
                onValueChange = editTaskEvents::onDescriptionChange,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = {
                    Text(text = "Description")
                }
            )
            CardEditor(
                title = "Date",
                icon = painterResource(id = R.drawable.ic_calendar),
                content = task.getDisplayDueDate(),
                onEditClick = {
                    calendarState.show()
                },
                highlightColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardEditor(
    title: String,
    icon: Painter,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(title, color = highlightColor) }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(painter = icon, contentDescription = title, tint = highlightColor)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EditTaskPreview() {
    EditTaskScreenContent(
        task = Task(
            title = "Demo Task",
            description = "Demo Description",
        ),
        popUpScreen = {},
        editTaskEvents = object : EditTaskEvents {
            override fun onTitleChange(newValue: String) {
                TODO("Not yet implemented")
            }

            override fun onDescriptionChange(newValue: String) {
                TODO("Not yet implemented")
            }

            override fun onDateChange(newValue: Date) {
                TODO("Not yet implemented")
            }

            override fun onDoneClick(popUpScreen: () -> Unit) {
                TODO("Not yet implemented")
            }

            override fun onDeleteClick(popUpScreen: () -> Unit) {
                TODO("Not yet implemented")
            }

        },
    )
}
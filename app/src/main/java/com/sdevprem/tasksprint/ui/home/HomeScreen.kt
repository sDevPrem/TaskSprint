package com.sdevprem.tasksprint.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sdevprem.tasksprint.ui.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Destination.EditTaskScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new Task",
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("Tasks")
                },
            )
        }
    ) { paddingValues ->
        val tasks by viewModel.tasks.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(tasks, key = { task -> task.id }) { taskItem ->
                TaskItem(
                    task = taskItem,
                    onCheckChange = { viewModel.onTaskCheckChange(taskItem) },
                    onTaskItemClicked = { task ->
                        navController.navigate(
                            Destination.EditTaskScreen.passArgument(task.id)
                        )
                    }
                )
            }
        }
    }
}
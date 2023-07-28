package com.sdevprem.tasksprint.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sdevprem.tasksprint.R
import com.sdevprem.tasksprint.common.ext.getFormattedSrtMthYr
import com.sdevprem.tasksprint.data.model.Task
import com.sdevprem.tasksprint.ui.navigation.Destination
import java.util.Date

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.homeUiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        navController = navController,
        state = state,
        onDecrementDate = viewModel::onDecrementDate,
        onIncrementDate = viewModel::onIncrementDate,
        onTaskCheckChange = viewModel::onTaskCheckChange,
        onLogOutClick = {
            viewModel.onLogOutClick()
            navController.navigate(Destination.AuthScreen.route) {
                launchSingleTop = true
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    navController: NavController,
    state: HomeUIState,
    onDecrementDate: () -> Unit,
    onIncrementDate: () -> Unit,
    onTaskCheckChange: (Task) -> Unit,
    onLogOutClick: () -> Unit
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
                actions = {
                    var showMenu by rememberSaveable { mutableStateOf(false) }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, null)
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Log out") },
                            onClick = onLogOutClick
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Header(
                    onDecrementDate = onDecrementDate,
                    onIncrementDate = onIncrementDate,
                    displayDate = state.date.getFormattedSrtMthYr()
                )
            }
            if (state.isLoading)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            else if (state.taskList.isEmpty())
                EmptyLayout(Modifier.weight(1f))
            else
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.taskList, key = { task -> task.id }) { taskItem ->
                        TaskItem(
                            task = taskItem,
                            onCheckChange = { onTaskCheckChange(taskItem) },
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
}

@Composable
private fun EmptyLayout(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit_calendar),
            contentDescription = "No task found in this month.",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(100.dp)
        )
        Text(
            text = "No task found in this month. Create one using + button.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun Header(
    onDecrementDate: () -> Unit,
    onIncrementDate: () -> Unit,
    displayDate: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDecrementDate) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_toward_left),
                contentDescription = "decrement date",
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = displayDate,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        IconButton(onClick = onIncrementDate) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_toward_right),
                contentDescription = "increment date",
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HeaderPreview() {
    Header(
        onDecrementDate = {},
        onIncrementDate = {},
        displayDate = Date().getFormattedSrtMthYr()
    )
}
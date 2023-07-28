package com.sdevprem.tasksprint.ui.edit_task

import java.util.Date

interface EditTaskEvents {
    fun onTitleChange(newValue: String)

    fun onDescriptionChange(newValue: String)

    fun onDateChange(newValue: Date)

    fun onDoneClick(popUpScreen: () -> Unit)

    fun onDeleteClick(popUpScreen: () -> Unit)

}
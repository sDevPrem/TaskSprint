package com.sdevprem.tasksprint.common.ext

import com.sdevprem.tasksprint.core.model.Task

fun Task.getDisplayDueDate(): String {
    return dueDate.getDisplayDate()
}


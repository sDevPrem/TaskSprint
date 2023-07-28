package com.sdevprem.tasksprint.common.ext

import android.icu.text.SimpleDateFormat
import com.sdevprem.tasksprint.data.model.Task
import java.util.Locale

fun Task.getDisplayDueDate(): String {
    return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(dueDate)
}

private const val DATE_FORMAT = "EEE, d MMM yyyy"


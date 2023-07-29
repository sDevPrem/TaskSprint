package com.sdevprem.tasksprint.core.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Task(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val timeCreated: Date = Date(),
    val dueDate: Date = Date(),
    val completed: Boolean = false,
    val userId: String = ""
)
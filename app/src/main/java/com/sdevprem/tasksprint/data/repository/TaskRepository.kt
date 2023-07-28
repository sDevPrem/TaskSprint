package com.sdevprem.tasksprint.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.sdevprem.tasksprint.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val auth: FirebaseAuth
) {
    val tasks: Flow<List<Task>>
        get() = auth.currentUser?.uid?.let {
            firestore.collection(COLLECTION_TASK)
                .orderBy("dueDate", Query.Direction.DESCENDING)
                .whereEqualTo(FIELD_USER_ID, it)
                .dataObjects()
        } ?: flow {
            emit(emptyList())
        }

    suspend fun getTask(id: String): Task? =
        firestore.collection(COLLECTION_TASK).document(id).get().await().toObject()

    suspend fun updateTask(task: Task) {
        firestore.collection(COLLECTION_TASK).document(task.id).set(task).await()
    }

    suspend fun saveTask(task: Task): String =
        firestore.collection(COLLECTION_TASK).add(
            task.copy(
                userId = auth.currentUser?.uid ?: ""
            )
        ).await().id

    suspend fun deleteTask(id: String) {
        firestore.collection(COLLECTION_TASK).document(id).delete().await()
    }

    companion object {
        private const val FIELD_USER_ID = "userId"
        private const val COLLECTION_TASK = "tasks"
    }
}
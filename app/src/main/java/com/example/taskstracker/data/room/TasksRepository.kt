package com.example.taskstracker.data.room

import com.example.taskstracker.utils.UiStatus
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun getAllTasks(): Flow<UiStatus<List<Task>>>

    fun insertTask(task: Task): Flow<UiStatus<Unit>>

    fun deleteTask(task: Task): Flow<UiStatus<Unit>>

}
package com.example.taskstracker.data.room

import com.example.taskstracker.utils.UiStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val dao: TasksDao
) : TasksRepository{

    override fun getAllTasks(): Flow<UiStatus<List<Task>>> = flow{
        emit(UiStatus.LOADING)
        try {
            val result = dao.getTasks()
            emit(UiStatus.SUCCESS(result.toTask()))
        } catch (e: Exception){
            emit(UiStatus.ERROR(e))
        }
    }

    override fun insertTask(task: Task): Flow<UiStatus<Unit>> = flow{
        emit(UiStatus.LOADING)
        try {
            dao.insertTask(task.toTable())
            emit(UiStatus.SUCCESS(Unit))
        } catch (e: Exception){
            emit(UiStatus.ERROR(e))
        }
    }

    override fun deleteTask(task: Task): Flow<UiStatus<Unit>> = flow{
        emit(UiStatus.LOADING)
        try {
            dao.deleteTask(task.toTable())
            emit(UiStatus.SUCCESS(Unit))
        } catch (e: Exception){
            emit(UiStatus.ERROR(e))
        }
    }

}
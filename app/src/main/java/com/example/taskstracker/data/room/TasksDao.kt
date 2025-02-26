package com.example.taskstracker.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<TasksTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TasksTable)

    @Delete
    suspend fun deleteTask(task: TasksTable)

}
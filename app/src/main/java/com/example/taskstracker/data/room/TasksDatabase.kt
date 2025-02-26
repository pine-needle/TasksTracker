package com.example.taskstracker.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TasksTable::class],
    version = 1
)
abstract class TasksDatabase: RoomDatabase() {

    abstract fun getTasksDao(): TasksDao

}
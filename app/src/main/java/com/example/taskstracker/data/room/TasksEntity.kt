package com.example.taskstracker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.format.DateTimeFormatter

@Entity(tableName = "tasks")
data class TasksTable(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val dueDate: String
)

fun Task.toTable(): TasksTable{
    return TasksTable(
        id = this.id,
        title = this.title,
        description = this.description,
        completed = this.completed,
        dueDate = this.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    )
}

package com.example.taskstracker.data.room

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    var completed: Boolean = false,
    val dueDate: LocalDate
)

fun List<TasksTable>.toTask(): List<Task>{
    return this.map {
        Task(
            it.id,
            it.title,
            it.description,
            it.completed,
            LocalDate.parse(it.dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
        )
    }
}

package com.example.taskstracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskstracker.data.room.Task
import com.example.taskstracker.databinding.TaskCardBinding
import java.time.format.DateTimeFormatter

class TasksAdapter(
    private val tasks: MutableList<Task> = mutableListOf(),
    private val onCompleteTask: (Task) -> Unit,
    private val onDeleteTask: (Task) -> Unit
) : RecyclerView.Adapter<TasksViewHolder>() {

    fun updateTasks(newTasks: List<Task>) {
        if (tasks != newTasks) {
            tasks.clear()
            tasks.addAll(newTasks)
         //notify dataset changed
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            TaskCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(
            tasks[position],
            onCompleteTask,
            onDeleteTask
        )
    }

}

// ViewHolder
class TasksViewHolder(
    private val binding: TaskCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        task: Task,
        onCompleteTask: (Task) -> Unit,
        onDeleteTask: (Task) -> Unit
    ) {
        binding.apply {
            taskTitle.text = task.title
            taskDescription.text = task.description
            dueDate.text = task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            if (task.completed) {
                status.text = "Completed"
                completeBtn.isEnabled = false
                completeBtn.visibility = View.GONE
            } else {
                status.text = "Not Completed"
                completeBtn.isEnabled = true
                completeBtn.visibility = View.VISIBLE
                completeBtn.setOnClickListener {
                    onCompleteTask(task)
                }
            }
            deleteBtn.setOnClickListener {
                onDeleteTask(task)
            }
        }
    }

}
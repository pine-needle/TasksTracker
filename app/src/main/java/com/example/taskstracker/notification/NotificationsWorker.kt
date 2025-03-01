package com.example.taskstracker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.taskstracker.CHANNEL_ID
import com.example.taskstracker.R
import kotlin.random.Random

class NotificationsWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context,workerParameters) {

    private val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager

    override fun doWork(): Result {
        val inputData = inputData
        val taskTitle = inputData.getString(TITLE)
        val taskDescription = inputData.getString(DESCRIPTION)
        val taskDueDate = inputData.getString(DUE_DATE)
        createNotificationsChannel()
        val id = Random.nextInt()
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setContentTitle(taskTitle)
            .setContentText("$taskDescription is due for $taskDueDate")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.baseline_calendar_month_24)
            .build()
        notificationManager.notify(id,notification)
        return Result.success()
    }

    private fun createNotificationsChannel(){
        val name = "Tasks channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID,name,importance)
        notificationManager.createNotificationChannel(channel)
    }

    companion object{
        const val TITLE = "task_title"
        const val DESCRIPTION = "task_description"
        const val DUE_DATE = "task_due_date"
    }

}
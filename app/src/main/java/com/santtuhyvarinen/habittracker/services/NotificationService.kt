package com.santtuhyvarinen.habittracker.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.activities.MainActivity
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel

class NotificationService : LifecycleService() {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var taskManager: TaskManager

    companion object {
        const val ONGOING_NOTIFICATION_ID = 12
        const val CHANNEL_ID = "HabitTrackerNotificationChannel"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        databaseManager = DatabaseManager(this)
        taskManager = TaskManager(databaseManager)

        val habitsObserver = Observer<List<HabitWithTaskLogs>> {
            taskManager.generateDailyTasks(it)
        }
        databaseManager.habitRepository.habitsWithTaskLogs.observe(this, habitsObserver)

        val tasksObserver = Observer<List<TaskModel>> {
            updateNotification(it.size)
        }
        taskManager.tasks.observe(this, tasksObserver)
    }

    private fun updateNotification(tasksLeft : Int) {
        val allTasksDone = tasksLeft == 0
        val contentText : String = if(!allTasksDone) {
            getString(R.string.notification_tasks_left, tasksLeft)
        } else {
            getString(R.string.notification_tasks_done)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(R.drawable.ic_tasks)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
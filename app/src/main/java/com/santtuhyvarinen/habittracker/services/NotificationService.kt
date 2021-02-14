package com.santtuhyvarinen.habittracker.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.activities.MainActivity
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import org.joda.time.DateTime

class NotificationService : LifecycleService() {

    private var tasksUpdatedDatetime = DateTime.now()
    private var habitsWithTaskLogs : List<HabitWithTaskLogs> = ArrayList()

    private lateinit var databaseManager: DatabaseManager
    private lateinit var taskManager: TaskManager
    private lateinit var minuteTickReceiver : BroadcastReceiver

    companion object {
        const val ONGOING_NOTIFICATION_ID = 12
        const val CHANNEL_ID = "HabitTrackerNotificationChannel"
        const val CHANNEL_NAME = "Habit Tracker Tasks"
        const val SERVICE_LOG_TAG = "notification_service"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        databaseManager = DatabaseManager(this)
        taskManager = TaskManager(databaseManager)

        val habitsObserver = Observer<List<HabitWithTaskLogs>> {
            habitsWithTaskLogs = it
            updateTasks()
        }
        databaseManager.habitRepository.habitsWithTaskLogs.observe(this, habitsObserver)

        val tasksObserver = Observer<List<TaskModel>> {
            updateNotification(it.size)
        }
        taskManager.tasks.observe(this, tasksObserver)


        //Broadcast Receiver. For updating the notification at midnight
        minuteTickReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val intentAction = intent.action?: return
                if (intentAction.compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    //Check if midnight has passed from last tasks update
                    val currentDate = DateTime.now()
                    if(!CalendarUtil.areSameDate(currentDate, tasksUpdatedDatetime)) {
                        Log.d(SERVICE_LOG_TAG, "Date changed. Updating notification")
                        updateTasks()
                    }
                }
            }
        }

        registerReceiver(minuteTickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    private fun updateTasks() {
        tasksUpdatedDatetime = DateTime.now()
        taskManager.generateDailyTasks(habitsWithTaskLogs)
    }

    private fun updateNotification(tasksLeft : Int) {
        val allTasksDone = tasksLeft == 0

        val smallIcon = if(allTasksDone) R.drawable.ic_notification_tasks_done else R.drawable.ic_notification_tasks
        val contentText = when(tasksLeft) {
            0 -> getString(R.string.notification_tasks_done)
            1 -> getString(R.string.notification_tasks_left_one)
            else -> getString(R.string.notification_tasks_left_plural, tasksLeft)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(smallIcon)
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
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(minuteTickReceiver)
    }
}
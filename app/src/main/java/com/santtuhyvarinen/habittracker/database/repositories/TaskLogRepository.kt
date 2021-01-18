package com.santtuhyvarinen.habittracker.database.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import com.santtuhyvarinen.habittracker.database.AppDatabase
import com.santtuhyvarinen.habittracker.database.dao.TaskLogDao
import com.santtuhyvarinen.habittracker.models.TaskLog

@Suppress("RedundantSuspendModifier")

class TaskLogRepository(private val taskLogDao: TaskLogDao) {
    val taskLogs = taskLogDao.getAll()

    @WorkerThread
    suspend fun createTaskLog(taskLog: TaskLog) : Long {
        val id = taskLogDao.create(taskLog)
        Log.d(AppDatabase.DATABASE_LOG_TAG, "TaskLog inserted to database with id $id")

        return id
    }

    @WorkerThread
    suspend fun deleteTaskLog(taskLog: TaskLog) : Int {
        val rows = taskLogDao.delete(taskLog)

        Log.d(AppDatabase.DATABASE_LOG_TAG, "$rows rows deleted from the database")

        return rows
    }

    @WorkerThread
    suspend fun updateTaskLog(taskLog: TaskLog) : Int {
        val rows = taskLogDao.update(taskLog)

        Log.d(AppDatabase.DATABASE_LOG_TAG, "$rows rows updated in the database")

        return rows
    }
    
    @WorkerThread
    suspend fun getTaskById(id : Long) : TaskLog? {
        return taskLogDao.getById(id)
    }
}
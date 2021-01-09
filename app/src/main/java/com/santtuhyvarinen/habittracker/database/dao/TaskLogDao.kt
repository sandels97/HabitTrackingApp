package com.santtuhyvarinen.habittracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.santtuhyvarinen.habittracker.models.TaskLog

@Dao
interface TaskLogDao {
    @Query("SELECT * FROM tasklog")
    fun getAll(): LiveData<List<TaskLog>>

    @Query("SELECT * FROM tasklog WHERE id IN (:id)")
    suspend fun getById(id : Long) : TaskLog?

    @Query("SELECT * FROM tasklog WHERE habitId IN (:habitId)")
    suspend fun getByHabit(habitId: Long) : List<TaskLog>

    @Insert
    suspend fun create(taskLog: TaskLog) : Long

    @Update
    suspend fun update(taskLog: TaskLog) : Int

    @Delete
    suspend fun delete(taskLog: TaskLog) : Int
}
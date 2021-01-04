package com.santtuhyvarinen.habittracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.santtuhyvarinen.habittracker.models.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE id IN (:id)")
    suspend fun getById(id : Long) : Habit?

    @Insert
    suspend fun create(habit: Habit) : Long

    @Update
    suspend fun update(habit: Habit) : Int

    @Delete
    suspend fun delete(habit: Habit) : Int
}
package com.santtuhyvarinen.habittracker.database

import androidx.room.*
import com.santtuhyvarinen.habittracker.models.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): List<Habit>

    @Query("SELECT * FROM habit WHERE id IN (:id)")
    fun getById(id : Long)

    @Insert
    fun create(habit: Habit)

    @Update
    fun update(habit: Habit)

    @Delete
    fun delete(habit: Habit)
}
package com.santtuhyvarinen.habittracker.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.santtuhyvarinen.habittracker.models.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE id IN (:id)")
    suspend fun getById(id : Long) : Habit

    @Insert
    suspend fun create(habit: Habit) : Long

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)
}
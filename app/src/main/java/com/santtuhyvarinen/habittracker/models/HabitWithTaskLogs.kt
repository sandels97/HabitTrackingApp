package com.santtuhyvarinen.habittracker.models

import androidx.room.*

data class HabitWithTaskLogs (
    @Embedded
    val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val taskLogs: List<TaskLog>
)
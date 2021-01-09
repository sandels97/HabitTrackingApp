package com.santtuhyvarinen.habittracker.models

import androidx.room.*

data class HabitWithTaskLogs (
    @Embedded
    val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val taskLogs: List<TaskLog>
)
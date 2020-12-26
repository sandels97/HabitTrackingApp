package com.santtuhyvarinen.habittracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey (
        entity = Habit::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("habit_id"),
        onDelete = ForeignKey.CASCADE
)])
data class TaskLog(@PrimaryKey(autoGenerate = true) val id : Long = 0) {

    @ColumnInfo(name = "habit_id")
    var habit_id : Long = 0

    @ColumnInfo(name = "score")
    var score : Int = 0

    @ColumnInfo(name = "status")
    var status : String = ""

    @ColumnInfo(name = "timestamp")
    var timestamp : Long = 0L
}
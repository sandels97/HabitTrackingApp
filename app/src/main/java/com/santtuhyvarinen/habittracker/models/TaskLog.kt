package com.santtuhyvarinen.habittracker.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey (
        entity = Habit::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("habitId"),
        onDelete = ForeignKey.CASCADE
)])
data class TaskLog(@PrimaryKey(autoGenerate = true) val id : Long = 0) {

    var habitId : Long = 0

    var score : Int = 0

    var status : String = ""

    var timestamp : Long = 0L
}
package com.santtuhyvarinen.habittracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(@PrimaryKey(autoGenerate = true) val id : Long = 0) {

    var name : String = ""

    var taskRecurrence : String = ""

    var priority : Int = 0

    var iconKey : String? = null

    var creationDate : Long = 0L

    var modificationDate : Long = 0L

    var score : Int = 0

    var disabled : Boolean = false

    override fun toString(): String {
        return "${name}, taskRecurrence = ${taskRecurrence}, priority = ${priority}, iconKey = ${iconKey}"
    }
}
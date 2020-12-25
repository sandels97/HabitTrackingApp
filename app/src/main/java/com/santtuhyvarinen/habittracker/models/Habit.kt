package com.santtuhyvarinen.habittracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(@PrimaryKey(autoGenerate = true) val id : Long = 0) {

    @ColumnInfo(name = "name")
    var name : String = ""
    @ColumnInfo(name = "task_recurrence")
    var taskRecurrence : String = ""

    @ColumnInfo(name = "priority")
    var priority : Int = 0

    @ColumnInfo(name = "icon_key")
    var iconKey : String? = null

    @ColumnInfo(name = "created")
    var creationDate : Long = 0L

    @ColumnInfo(name = "modified")
    var modificationDate : Long = 0L

    override fun toString(): String {
        return "${name}, taskRecurrence = ${taskRecurrence}, priority = ${priority}, iconKey = ${iconKey}"
    }
}
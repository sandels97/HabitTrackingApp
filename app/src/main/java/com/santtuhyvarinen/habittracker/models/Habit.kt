package com.santtuhyvarinen.habittracker.models

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Habit(@PrimaryKey val id : Long = 0) {

    @ColumnInfo(name = "name")
    var name : String = ""

    @ColumnInfo(name = "task_recurrence")
    var taskRecurrence : String = ""

    @ColumnInfo(name = "priority")
    var priority : Int = 0

    @ColumnInfo(name = "icon_key")
    var iconKey : String = ""

    @ColumnInfo(name = "created")
    var creationDate : Long = 0L

    @ColumnInfo(name = "modified")
    var modificationDate : Long = 0L

    @Ignore
    var iconDrawable : Drawable? = null
}
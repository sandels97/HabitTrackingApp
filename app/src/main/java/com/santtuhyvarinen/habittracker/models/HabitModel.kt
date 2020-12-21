package com.santtuhyvarinen.habittracker.models

import android.graphics.drawable.Drawable

class HabitModel {
    var id : Long = 0L
    var name : String = ""
    var taskRecurrence : String = ""
    var priority : Int = 0

    var iconKey : String = ""

    var creationDate : Long = 0L
    var modificationDate : Long = 0L

    var iconDrawable : Drawable? = null
}
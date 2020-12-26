package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import com.santtuhyvarinen.habittracker.R

class HabitInfoManager(context: Context) {

    var priorityLevels : Array<String> = Array (0) { "" }

    init {
        priorityLevels = context.resources.getStringArray(R.array.PriorityLevels)
    }

    fun getMaxPriorityLevel() : Int {
        return priorityLevels.size - 1
    }

    fun getCurrentPriorityLevelText(priorityValue : Int) : String {
        if(priorityLevels.isEmpty()) return ""

        val index = priorityValue.coerceAtLeast(0).coerceAtMost(priorityLevels.size-1)
        return priorityLevels[index]
    }
}
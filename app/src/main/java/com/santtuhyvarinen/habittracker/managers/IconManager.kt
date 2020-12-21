package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import android.util.Log
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.IconModel

class IconManager {

    val iconModels = ArrayList<IconModel>()

    fun loadIcons(context: Context) {
        if(iconModels.isNotEmpty()) return

        val iconKeys = context.resources.getStringArray(R.array.HabitIconKeys)
        val iconTitles = context.resources.getStringArray(R.array.HabitIconTitles)
        val iconsTypedArray = context.resources.obtainTypedArray(R.array.HabitIcons)

        for(i in iconTitles.indices) {
            val key = iconKeys[i]
            val title = iconTitles[i]
            val drawable = iconsTypedArray.getDrawable(i)!!
            val iconModel = IconModel(key, drawable, title)

            iconModels.add(iconModel)
        }

        iconsTypedArray.recycle()
    }

    fun getIconModelByKey(key : String) : IconModel? {
        for(icon in iconModels) {
            if(icon.key == key) return icon
        }

        return null
    }
}
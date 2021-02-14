package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import android.graphics.drawable.Drawable
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.IconModel

class IconManager(context: Context) {

    val iconModels = ArrayList<IconModel>()

    init {
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

    fun getIconByKey(key : String?) : Drawable? {
        if(key == null) return null

        return getIconModelByKey(key)?.drawable
    }

    fun getIconModelByKey(key : String?) : IconModel? {
        if(key == null) return null

        for(iconModel in iconModels) {
            if(iconModel.key == key) return iconModel
        }

        return null
    }
}
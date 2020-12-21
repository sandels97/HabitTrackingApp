package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.IconSelectionAdapter
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.IconModel
import kotlinx.android.synthetic.main.layout_icon_picker.view.*

class IconPickerView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private val iconSelectionAdapter : IconSelectionAdapter

    var iconManager : IconManager? = null
    set(value) {
        field = value

        value ?: return

        iconSelectionAdapter.data = value.iconModels
        iconSelectionAdapter.notifyDataSetChanged()
    }

    init {
        inflate(context, R.layout.layout_icon_picker, this)

        recyclerView.layoutManager = GridLayoutManager(context, 5)

        iconSelectionAdapter = IconSelectionAdapter(context, ArrayList())
        recyclerView.adapter = iconSelectionAdapter
    }

    fun setSelectedIcon(iconModel: IconModel?) {
        iconSelectionAdapter.selectedIconModel = iconModel
        iconSelectionAdapter.notifyDataSetChanged()
    }

    fun setIconSelectedListener(iconSelectedListener: IconSelectionAdapter.IconSelectedListener) {
        iconSelectionAdapter.iconSelectedListener = iconSelectedListener
    }
}
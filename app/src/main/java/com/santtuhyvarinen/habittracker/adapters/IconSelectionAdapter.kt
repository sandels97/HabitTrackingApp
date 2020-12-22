package com.santtuhyvarinen.habittracker.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.IconModel

class IconSelectionAdapter(var context: Context, var data : List<IconModel>) : RecyclerView.Adapter<IconSelectionAdapter.ViewHolder>() {

    var selectedIconModel : IconModel? = null

    var iconSelectedListener : IconSelectedListener? = null
    interface IconSelectedListener {
        fun iconSelected(iconModel: IconModel?)
    }

    class ViewHolder(var button : ImageButton) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = LayoutInflater.from(context).inflate(R.layout.item_button_icon, parent, false) as ImageButton

        return ViewHolder(button)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val iconModel = data[position]

        val imageButton = holder.button

        imageButton.setImageDrawable(iconModel.drawable)
        imageButton.contentDescription = iconModel.contentDescription
        imageButton.isSelected = selectedIconModel == iconModel

        val drawableTint = if(imageButton.isSelected) Color.WHITE else Color.BLACK
        imageButton.imageTintList = ColorStateList.valueOf(drawableTint)

        imageButton.setOnClickListener {
            val icon = if(imageButton.isSelected) null else iconModel

            selectedIconModel = icon
            iconSelectedListener?.iconSelected(icon)

            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
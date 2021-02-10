package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.DateStatusModel
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import org.joda.time.DateTime


class HabitTimelineView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var habit : HabitWithTaskLogs? = null
    private var dateStatusModels : Array<DateStatusModel> = Array(0) { DateStatusModel("", "", TaskUtil.STATUS_NONE) }

    var fromDate : DateTime = DateTime.now()
    var days = 7
    var iconSize = 20

    fun setup(habitWithTaskLogs: HabitWithTaskLogs) {
        habit = habitWithTaskLogs

        dateStatusModels = TaskUtil.getDateStatusModelsForHabit(context, habitWithTaskLogs, fromDate, days)
        invalidate()
    }

    private var successIcon = ContextCompat.getDrawable(context, R.drawable.ic_task_success)!!
    private var failIcon = ContextCompat.getDrawable(context, R.drawable.ic_task_fail)!!
    private var skipIcon = ContextCompat.getDrawable(context, R.drawable.ic_task_skip)!!

    private val paint = Paint()
    private val textPaint = TextPaint()
    init {

        paint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context, R.color.colorIconTintLight)
        paint.style = Paint.Style.FILL

        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

        val attributes: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.HabitTimelineView)

        context.withStyledAttributes(attributeSet, R.styleable.HabitTimelineView) {
            textPaint.textSize = getDimension(R.styleable.HabitTimelineView_textSize, 18f)
            textPaint.color = getColor(R.styleable.HabitTimelineView_textColor, Color.BLACK)
            days = getInt(R.styleable.HabitTimelineView_days, days)
            iconSize = getDimension(R.styleable.HabitTimelineView_iconSize, 20f).toInt()
        }

        attributes.recycle()

        //Init icons
        successIcon.mutate().setTint(ContextCompat.getColor(context, R.color.colorSuccess))

        val tintColor = ContextCompat.getColor(context, R.color.colorIconTint)
        failIcon.mutate().setTint(tintColor)
        skipIcon.mutate().setTint(tintColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(days <= 0 || habit == null) return

        val columnWidth = width / days
        val iconMargin = iconSize / 8

        //Draw day headers and icons
        for(i in dateStatusModels.indices) {
            if(i == dateStatusModels.size-1) {
                textPaint.typeface = Typeface.DEFAULT_BOLD
            }

            val dateStatusModel = dateStatusModels[i]
            //Day header
            val label = dateStatusModel.label
            val underLabel = dateStatusModel.underLabel

            val textSize = textPaint.textSize
            val textTop = textSize
            val textLeft = i * columnWidth + (columnWidth / 2f)
            canvas.drawText(label, textLeft, textTop, textPaint)
            canvas.drawText(underLabel, textLeft, textTop + textSize, textPaint)


            //Icon
            val status = dateStatusModel.status
            val icon : Drawable?
            when(status) {
                TaskUtil.STATUS_SUCCESS -> icon = successIcon
                TaskUtil.STATUS_SKIPPED -> icon = skipIcon
                TaskUtil.STATUS_FAILED -> icon = failIcon
                else -> icon = null
            }

            val top = (height/2) - (iconSize/2)
            val left = i * columnWidth + (columnWidth / 2) - (iconSize / 2)
            val bottom = top + iconSize
            val right = left + iconSize

            if(icon != null) {
                icon.setBounds(
                        left + iconMargin,
                        top + iconMargin,
                        right - iconMargin,
                        bottom - iconMargin)

                icon.draw(canvas)
            } else {
                val circleRadius = iconSize / 8f
                canvas.drawCircle(left + iconSize/2f, top + iconSize/2f, circleRadius, paint)
            }
        }

        textPaint.typeface = Typeface.DEFAULT
    }
}
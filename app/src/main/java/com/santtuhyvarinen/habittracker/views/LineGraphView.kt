package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.santtuhyvarinen.habittracker.R

class LineGraphView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var headers : List<Int> = ArrayList()
    var values : List<Int> = ArrayList()

    private val paint = Paint()

    var columns = 7
    set(value) {
        field = value.coerceAtLeast(0)
    }
    var rows = 5
    set(value) {
        field = value.coerceAtLeast(0)
    }

    var lineColor : Int
    var lineStrokeWidth : Float

    var backgroundLineColor : Int
    var backgroundLineStrokeWidth : Float

    var dotRadius : Float = 15f

    init {

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND

        val attributes: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LineGraphView)

        lineColor = attributes.getColor(R.styleable.LineGraphView_lineColor, Color.BLACK)
        lineStrokeWidth = attributes.getDimension(R.styleable.LineGraphView_lineStrokeWidth, 10f)
        backgroundLineColor = attributes.getColor(R.styleable.LineGraphView_backgroundLineColor, Color.GRAY)
        backgroundLineStrokeWidth = attributes.getDimension(R.styleable.LineGraphView_backgroundLineStrokeWidth, 2f)

        dotRadius = attributes.getDimension(R.styleable.LineGraphView_dotRadius, 15f)
        columns = attributes.getInt(R.styleable.LineGraphView_columns, columns)

        attributes.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val columnWidth = (width - paddingLeft - paddingRight) / columns
        val rowHeight = (height - paddingTop - paddingBottom) / rows

        val bottomHeight = (height - paddingBottom).toFloat()
        //Draw lines

        //Background lines
        paint.color = backgroundLineColor
        paint.strokeWidth = backgroundLineStrokeWidth

        canvas.drawLine(paddingLeft.toFloat(), bottomHeight, width - paddingRight.toFloat(), bottomHeight, paint)

        //Draw columns
        for(column in 0 until columns) {
            val x = paddingLeft + (column * columnWidth) + columnWidth / 2f
            canvas.drawLine(x, bottomHeight, x, paddingTop.toFloat(), paint)
        }

        //Graph
        paint.color = lineColor
        paint.strokeWidth = lineStrokeWidth

        var previousX = 0f
        var previousY = bottomHeight
        for (i in 0 until columns) {
            val value = if(i < values.size) values[i] else 0

            val x = paddingLeft + (i * columnWidth) + columnWidth / 2f
            val y = bottomHeight - (rowHeight * value).toFloat()

            if(i > 0) canvas.drawLine(previousX, previousY, x, y, paint)

            canvas.drawCircle(x, y, dotRadius, paint)

            previousX = x
            previousY = y
        }
    }
}
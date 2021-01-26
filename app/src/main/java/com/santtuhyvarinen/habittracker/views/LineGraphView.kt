package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.LineGraphDataModel

class LineGraphView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    var lineGraphData : List<LineGraphDataModel> = ArrayList()

    private val paint = Paint()
    private val textPaint = TextPaint()
    private val textBounds = Rect()

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

        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

        val attributes: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LineGraphView)

        lineColor = attributes.getColor(R.styleable.LineGraphView_lineColor, Color.BLACK)
        lineStrokeWidth = attributes.getDimension(R.styleable.LineGraphView_lineStrokeWidth, 10f)
        backgroundLineColor = attributes.getColor(R.styleable.LineGraphView_backgroundLineColor, Color.GRAY)
        backgroundLineStrokeWidth = attributes.getDimension(R.styleable.LineGraphView_backgroundLineStrokeWidth, 2f)

        textPaint.textSize = attributes.getDimension(R.styleable.LineGraphView_textSize, 18f)
        textPaint.color = attributes.getColor(R.styleable.LineGraphView_textColor, Color.BLACK)

        dotRadius = attributes.getDimension(R.styleable.LineGraphView_dotRadius, 15f)
        columns = attributes.getInt(R.styleable.LineGraphView_columns, columns)

        attributes.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val textMargin = textPaint.textSize
        val columnWidth = (width - paddingLeft - paddingRight) / columns
        val rowHeight = (height - paddingTop - paddingBottom - textMargin*2) / rows

        val bottomHeight = (height - paddingBottom - textMargin*2)
        //Draw lines

        //Background lines
        paint.color = backgroundLineColor
        paint.strokeWidth = backgroundLineStrokeWidth

        canvas.drawLine(paddingLeft.toFloat(), bottomHeight, width - paddingRight.toFloat(), bottomHeight, paint)

        val useMinimizedLabels = shouldUseMinimizedLabels()

        //Draw columns
        for(column in 0 until columns) {
            val x = paddingLeft + (column * columnWidth) + columnWidth / 2f
            canvas.drawLine(x, bottomHeight, x, paddingTop.toFloat(), paint)

            if(useMinimizedLabels) {
                if(column > 0 && column < columns-1) continue
            }
            
            //Column labels
            val label = if(column < lineGraphData.size) lineGraphData[column].label else continue
            val underLabel = if(column < lineGraphData.size) lineGraphData[column].underLabel else ""

            textPaint.getTextBounds(label, 0, label.length, textBounds)

            val labelTextHeight = textBounds.height()

            val textY = bottomHeight + labelTextHeight - textBounds.exactCenterY()

            textPaint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText(label, x, textY, textPaint)
            textPaint.typeface = Typeface.DEFAULT

            textPaint.getTextBounds(underLabel, 0, underLabel.length, textBounds)
            canvas.drawText(underLabel, x, textY + labelTextHeight - textBounds.exactCenterY(), textPaint)
        }

        //Graph
        paint.color = lineColor
        paint.strokeWidth = lineStrokeWidth

        var previousX = 0f
        var previousY = bottomHeight
        for (i in 0 until columns) {
            val value = if(i < lineGraphData.size) lineGraphData[i].value else 0

            val x = paddingLeft + (i * columnWidth) + columnWidth / 2f
            val y = bottomHeight - (rowHeight * value)

            if(i > 0) canvas.drawLine(previousX, previousY, x, y, paint)

            canvas.drawCircle(x, y, dotRadius, paint)


            previousX = x
            previousY = y
        }

        //Draw value texts
        for (i in 0 until columns) {
            val value = if (i < lineGraphData.size) lineGraphData[i].value else 0
            val label = value.toString()

            val x = paddingLeft + (i * columnWidth) + columnWidth / 2f
            val y = bottomHeight - (rowHeight * value)

            textPaint.getTextBounds(label, 0, label.length, textBounds)
            canvas.drawText(label, x, y - dotRadius*3 - textBounds.exactCenterY(), textPaint)
        }
    }

    //Checks if the label text will fit the columns
    private fun shouldUseMinimizedLabels() : Boolean {
        if(lineGraphData.isEmpty()) return false

        val underLabelText = lineGraphData[0].underLabel
        textPaint.getTextBounds(underLabelText, 0, underLabelText.length, textBounds)

        val labelsEstimatedWidth = textBounds.width() * lineGraphData.size

        return labelsEstimatedWidth >= width
    }
}
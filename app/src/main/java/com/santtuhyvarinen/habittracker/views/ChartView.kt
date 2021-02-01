package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.ChartDataModel

class ChartView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        const val CHART_TYPE_LINE = 0
        const val CHART_TYPE_COLUMN = 1
    }

    var chartType = CHART_TYPE_LINE

    var chartData : List<ChartDataModel> = ArrayList()

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

    var lineColor = Color.BLACK
    var lineStrokeWidth = 10f

    var backgroundLineColor = Color.GRAY
    var backgroundLineStrokeWidth = 2f

    var dotRadius : Float = 15f

    init {
        context.withStyledAttributes(attributeSet, R.styleable.ChartView) {
            lineColor = getColor(R.styleable.ChartView_lineColor, Color.BLACK)
            lineStrokeWidth = getDimension(R.styleable.ChartView_lineStrokeWidth, 10f)
            backgroundLineColor = getColor(R.styleable.ChartView_backgroundLineColor, Color.GRAY)
            backgroundLineStrokeWidth = getDimension(R.styleable.ChartView_backgroundLineStrokeWidth, 2f)
            chartType = getInt(R.styleable.ChartView_chartType, CHART_TYPE_LINE)

            textPaint.textSize = getDimension(R.styleable.ChartView_textSize, 18f)
            textPaint.color = getColor(R.styleable.ChartView_textColor, Color.BLACK)

            dotRadius = getDimension(R.styleable.ChartView_dotRadius, 15f)
            columns = getInt(R.styleable.ChartView_columns, columns)
            rows = getInt(R.styleable.ChartView_rows, rows)
        }

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND

        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val textMargin = textPaint.textSize
        val columnWidth = (width - paddingLeft - paddingRight) / columns
        val rowHeight = (height - paddingTop - paddingBottom - textMargin*2) / rows

        val bottomHeight = (height - paddingBottom - textMargin*2)

        //Background lines
        paint.color = backgroundLineColor
        paint.strokeWidth = backgroundLineStrokeWidth

        canvas.drawLine(paddingLeft.toFloat(), bottomHeight, width - paddingRight.toFloat(), bottomHeight, paint)

        val useMinimizedLabels = shouldUseMinimizedLabels()

        //Draw columns
        for(column in 0 until columns) {
            //Column lines
            val x = paddingLeft + (column * columnWidth) + columnWidth / 2f
            canvas.drawLine(x, bottomHeight, x, paddingTop.toFloat(), paint)

            if(useMinimizedLabels) {
                if(column > 0 && column < columns-1) continue
            }
            
            //Column labels
            val label = if(column < chartData.size) chartData[column].label else continue
            val underLabel = if(column < chartData.size) chartData[column].underLabel else ""

            val labelTextHeight = textPaint.textSize

            val textY = bottomHeight + labelTextHeight

            textPaint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText(label, x, textY, textPaint)
            textPaint.typeface = Typeface.DEFAULT

            canvas.drawText(underLabel, x, textY + labelTextHeight, textPaint)
        }

        paint.color = lineColor
        paint.strokeWidth = lineStrokeWidth

        when(chartType) {
            CHART_TYPE_LINE -> {
                //Draw line chart data
                var previousX = 0f
                var previousY = bottomHeight
                for (i in 0 until columns) {
                    val value = if(i < chartData.size) chartData[i].value else 0

                    val x = paddingLeft + (i * columnWidth) + columnWidth / 2f
                    val y = bottomHeight - (rowHeight * value)

                    if(i > 0) canvas.drawLine(previousX, previousY, x, y, paint)

                    canvas.drawCircle(x, y, dotRadius, paint)

                    previousX = x
                    previousY = y
                }
            }

            CHART_TYPE_COLUMN -> {
                //Draw columns chart data
                val margin = columnWidth / 10

                for (i in 0 until columns) {
                    val value = if(i < chartData.size) chartData[i].value else 0

                    val left = paddingLeft + (i * columnWidth).toFloat() + margin
                    val top = bottomHeight - (rowHeight * value)
                    val right = left + columnWidth - (margin*2)
                    val bottom = bottomHeight

                    canvas.drawRoundRect(left, top, right, bottom, 10f, 10f, paint)
                }
            }
        }


        //Draw value texts
        for (i in 0 until columns) {
            val value = if (i < chartData.size) chartData[i].value else 0
            val label = value.toString()

            val x = paddingLeft + (i * columnWidth) + columnWidth / 2f
            val y = bottomHeight - (rowHeight * value)

            textPaint.getTextBounds(label, 0, label.length, textBounds)
            canvas.drawText(label, x, y - dotRadius*3 - textBounds.exactCenterY(), textPaint)
        }
    }

    //Checks if the label text will fit the columns
    private fun shouldUseMinimizedLabels() : Boolean {
        if(chartData.isEmpty()) return false

        val underLabelText = chartData[0].underLabel
        textPaint.getTextBounds(underLabelText, 0, underLabelText.length, textBounds)

        val labelsEstimatedWidth = textBounds.width() * chartData.size

        return labelsEstimatedWidth >= width
    }
}
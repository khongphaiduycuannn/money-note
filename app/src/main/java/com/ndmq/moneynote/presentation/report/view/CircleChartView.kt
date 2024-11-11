package com.ndmq.moneynote.presentation.report.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sqrt

class CircleChartView : View {

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var radius = 0f
    private var padding = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var outRadius = 0f
    private var outRadiusSelected = 0f
    private var inRadius1 = 0f
    private var centerRadius = 0f
    private var inRadius2 = 0f

    private var circleOut = RectF()
    private var circleOutSelected = RectF()
    private var circleCenter = RectF()
    private var circleIn1 = RectF()
    private var circleIn2 = RectF()

    private fun init() {
        setChartParts(
            listOf(
                ChartPart(1, Color.RED, 20f),
                ChartPart(2, Color.BLUE, 20f),
                ChartPart(3, Color.CYAN, 20f),
                ChartPart(4, Color.LTGRAY, 20f),
                ChartPart(5, Color.GREEN, 20f),
            )
        )
    }

    private val paints = mutableListOf<Paint>()
    private val alphaPaints = mutableListOf<Paint>()

    private val parts = mutableListOf<ChartPart>()

    private var selectedPart: ChartPart? = null
    private var onPartSelected: (ChartPart) -> Unit = {}

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        radius = (width - paddingLeft - paddingRight) / 2f
        padding = (radius / 12.5).toFloat()
        centerX = paddingLeft + radius
        centerY = paddingTop + radius

        outRadius = radius - padding
        circleOut = RectF(
            centerX - outRadius,
            centerY - outRadius,
            centerX + outRadius,
            centerY + outRadius
        )

        outRadiusSelected = radius - padding / 1.5f
        circleOutSelected = RectF(
            centerX - outRadiusSelected,
            centerY - outRadiusSelected,
            centerX + outRadiusSelected,
            centerY + outRadiusSelected
        )

        inRadius1 = (radius - padding) * 5.15f / 11
        circleIn1 = RectF(
            centerX - inRadius1,
            centerY - inRadius1,
            centerX + inRadius1,
            centerY + inRadius1
        )

        centerRadius = (radius - padding) * 5.15f / 11
        circleCenter = RectF(
            centerX - centerRadius,
            centerY - centerRadius,
            centerX + centerRadius,
            centerY + centerRadius
        )

        inRadius2 = (radius - padding) * 3 / 7
        circleIn2 = RectF(
            centerX - inRadius2,
            centerY - inRadius2,
            centerX + inRadius2,
            centerY + inRadius2
        )
    }

    override fun onDraw(canvas: Canvas) {
        var prevAngle = -90f
        parts.forEachIndexed { index, chartPart ->
            canvas.drawArc(
                if (chartPart == selectedPart) circleOutSelected else circleOut,
                prevAngle + 1,
                chartPart.percent * 3.6f - 1,
                true,
                paints[index]
            )
            prevAngle += chartPart.percent * 3.6f
        }

        canvas.drawArc(circleIn1, -90f, 360f, true, Paint().apply {
            color = Color.WHITE
        })

        parts.forEachIndexed { index, chartPart ->
            canvas.drawArc(
                circleCenter,
                prevAngle + 1,
                chartPart.percent * 3.6f - 1,
                true,
                alphaPaints[index]
            )
            prevAngle += chartPart.percent * 3.6f
        }

        canvas.drawArc(circleIn2, -90f, 360f, true, Paint().apply {
            color = Color.WHITE
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val dis = sqrt(
                (event.x - centerX) * (event.x - centerX) + (event.y - centerY) * (event.y - centerY)
            )
            if (dis > radius) {
                selectedPart = null
                invalidate()
                return super.onTouchEvent(event)
            }

            var angle = radianToDegree(atan((event.y - centerY) / (event.x - centerX))) + 90
            if (centerX > event.x) angle += 180

            var currentPercent = 0f
            parts.forEach {
                if (angle in currentPercent * 3.6f..(currentPercent + it.percent) * 3.6f) {
                    if (selectedPart != it) {
                        selectedPart = it
                        onPartSelected(it)
                    }
                }
                currentPercent += it.percent
            }
            invalidate()
        }
        return super.onTouchEvent(event)
    }

    fun setOnPartSelected(func: (ChartPart) -> Unit) {
        onPartSelected = func
    }

    fun setChartParts(list: List<ChartPart>) {
        parts.clear()
        parts.addAll(list)

        paints.clear()
        alphaPaints.clear()
        list.forEach {
            paints.add(Paint().apply {
                style = Paint.Style.FILL
                isAntiAlias = true
                color = it.color
            })

            alphaPaints.add(Paint().apply {
                style = Paint.Style.FILL
                isAntiAlias = true
                color = getAlphaColor(it.color)
            })
        }

        invalidate()
    }

    private fun getAlphaColor(color: Int): Int {
        return ((color and 0x00FFFFFF) or (128 shl 24))
    }

    private fun radianToDegree(rad: Float): Float {
        return (rad / PI * 180f).toFloat()
    }
}
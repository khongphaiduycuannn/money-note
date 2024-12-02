package com.ndmq.moneynote.presentation.report.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.dto.ChartPart
import com.ndmq.moneynote.presentation.report.view.MathUtil.degreeToRadian
import com.ndmq.moneynote.presentation.report.view.MathUtil.getC
import com.ndmq.moneynote.presentation.report.view.MathUtil.radianToDegree
import com.ndmq.moneynote.utils.constant.formatNumberWithDots
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("DrawAllocation")
class CircleChartView : View {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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

    private val dialogPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.backgroundChartDialog)
        }
    }
    private val dialogPath = Path()

    private val defaultPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 3f
            isAntiAlias = true
            color = Color.LTGRAY
        }
    }

    private val defaultAlphaPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 3f
            isAntiAlias = true
            color = getAlphaColor(Color.LTGRAY)
        }
    }

    private val paintTextPercent by lazy {
        TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.defaultTextColor)
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
    }

    private val paintTextCategory by lazy {
        TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.defaultTextColor)
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }

    private val paintTextDialog by lazy {
        TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.highlightTextColor)
            textSize = 26f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
    }

    private val paintBoldTextDialog by lazy {
        TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.highlightTextColor)
            textSize = 26f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }

    private val paints = mutableListOf<Paint>()
    private val alphaPaints = mutableListOf<Paint>()

    private val parts = mutableListOf<ChartPart>()

    private var selectedPart: ChartPart? = null
    private var onPartSelected: (ChartPart) -> Unit = {}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        radius = (width - paddingLeft - paddingRight) / 2f
        padding = (radius / 12.5).toFloat()
        centerX = paddingLeft + radius
        centerY = (height / 2).toFloat()

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
        if (parts.isEmpty())
            canvas.drawArc(circleOut, -90f, 360f, true, defaultPaint)

        drawOutCircle(canvas)
        drawChartPartInformation(canvas)

        canvas.drawArc(circleIn1, -90f, 360f, true, Paint().apply {
            color = Color.WHITE
        })

        drawInAlphaCircle(canvas)

        canvas.drawArc(circleIn2, -90f, 360f, true, Paint().apply {
            color = Color.WHITE
        })

        drawPathInfoDialog(canvas)
    }

    private fun drawPathInfoDialog(canvas: Canvas) {
        if (selectedPart == null) return

        var prevAngle = -90f
        parts.forEachIndexed { _, chartPart ->
            if (chartPart == selectedPart) {
                var bx = centerX + (radius * sin(degreeToRadian(prevAngle + 90f)) + radius * sin(
                    degreeToRadian(prevAngle + 90f + chartPart.percent * 3.6f)
                )) / 2
                val by = centerY - (radius * cos(degreeToRadian(prevAngle + 90f)) + radius * cos(
                    degreeToRadian(prevAngle + 90f + chartPart.percent * 3.6f)
                )) / 2
                if ((prevAngle + 90f) in 0f..0.1f && chartPart.percent in 49.9f..50.1f) {
                    bx += 10
                }
                if ((prevAngle + 90f) in 179.9f..180.1f && chartPart.percent in 49.9f..50.1f) {
                    bx -= 10
                }

                val c = if (chartPart.percent <= 50) {
                    getC(centerX, centerY, bx, by, radius / 1.65f)
                } else {
                    getC(centerX, centerY, bx, by, radius / 1.65f, true)
                }

                val percent =
                    "${BigDecimal(chartPart.percent.toDouble()).setScale(2, RoundingMode.HALF_UP)}%"
                val category = chartPart.categoryName
                val total = "${formatNumberWithDots(chartPart.total)}$"
                val width = max(
                    max(
                        paintTextDialog.measureText(percent),
                        paintTextDialog.measureText(category)
                    ), paintBoldTextDialog.measureText(total)
                )

                dialogPath.reset()
                dialogPath.moveTo(c[0], c[1])
                dialogPath.lineTo(c[0] + 12, c[1] + 16)
                dialogPath.lineTo(c[0] + width / 2 + 20, c[1] + 16)
                dialogPath.quadTo(
                    c[0] + width / 2 + 28,
                    c[1] + 16,
                    c[0] + width / 2 + 28,
                    c[1] + 24
                )
                dialogPath.lineTo(c[0] + width / 2 + 28, c[1] + 152)
                dialogPath.quadTo(
                    c[0] + width / 2 + 28,
                    c[1] + 160,
                    c[0] + width / 2 + 20,
                    c[1] + 160
                )
                dialogPath.lineTo(c[0] - width / 2 - 20, c[1] + 160)
                dialogPath.quadTo(
                    c[0] - width / 2 - 28,
                    c[1] + 160,
                    c[0] - width / 2 - 28,
                    c[1] + 152
                )
                dialogPath.lineTo(c[0] - width / 2 - 28, c[1] + 24)
                dialogPath.quadTo(
                    c[0] - width / 2 - 28,
                    c[1] + 16,
                    c[0] - width / 2 - 20,
                    c[1] + 16
                )
                dialogPath.lineTo(c[0] - 12, c[1] + 16)
                dialogPath.lineTo(c[0], c[1])
                canvas.drawPath(dialogPath, dialogPaint)

                selectedPart?.let {
                    canvas.drawText(category, c[0] - width / 2, c[1] + 58, paintTextDialog)
                    canvas.drawText(total, c[0] - width / 2, c[1] + 98, paintBoldTextDialog)
                    canvas.drawText(percent, c[0] - width / 2, c[1] + 140, paintTextDialog)
                }
            }
            prevAngle += chartPart.percent * 3.6f
        }
    }

    private fun drawOutCircle(canvas: Canvas) {
        var prevAngle = -90f
        parts.forEachIndexed { index, chartPart ->
            canvas.drawArc(
                if (chartPart == selectedPart) circleOutSelected else circleOut,
                prevAngle + 1f,
                chartPart.percent * 3.6f - 1f,
                true,
                paints[index]
            )
            prevAngle += chartPart.percent * 3.6f
        }
    }

    private fun drawChartPartInformation(canvas: Canvas) {
        var prevAngle = -90f
        parts.forEachIndexed { index, chartPart ->
            if (chartPart.percent >= 10) {
                var bx = centerX + (radius * sin(degreeToRadian(prevAngle + 90f)) + radius * sin(
                    degreeToRadian(prevAngle + 90f + chartPart.percent * 3.6f)
                )) / 2
                val by = centerY - (radius * cos(degreeToRadian(prevAngle + 90f)) + radius * cos(
                    degreeToRadian(prevAngle + 90f + chartPart.percent * 3.6f)
                )) / 2
                if ((prevAngle + 90f) in 0f..0.1f && chartPart.percent in 49.9f..50.1f) {
                    bx += 10
                }
                if ((prevAngle + 90f) in 179.9f..180.1f && chartPart.percent in 49.9f..50.1f) {
                    bx -= 10
                }

                val percent =
                    BigDecimal(chartPart.percent.toDouble()).setScale(2, RoundingMode.HALF_UP)
                if (chartPart.percent <= 50) {
                    val c = getC(centerX, centerY, bx, by, radius / 7 * 8)
                    canvas.drawLine(centerX, centerY, c[0], c[1], paints[index])
                    if (c[0] - centerX > 0) {
                        canvas.drawLine(c[0], c[1], c[0] + 30, c[1], paints[index])
                        canvas.drawText(
                            "$percent %", c[0] + 40, c[1] - 5, paintTextPercent
                        )
                        canvas.drawText(
                            chartPart.categoryName, c[0] + 40, c[1] + 20, paintTextCategory
                        )
                    } else {
                        canvas.drawLine(c[0], c[1], c[0] - 30, c[1], paints[index])
                        val widthPercent = paintTextPercent.measureText("$percent %")
                        val widthCategory = paintTextCategory.measureText(chartPart.categoryName)
                        val startX = c[0] - 40 - max(widthCategory, widthPercent)
                        canvas.drawText(
                            "$percent %", startX, c[1] - 5, paintTextPercent
                        )
                        canvas.drawText(
                            chartPart.categoryName, startX, c[1] + 20, paintTextCategory
                        )
                    }
                } else {
                    val c = getC(centerX, centerY, bx, by, radius / 7 * 8, true)
                    canvas.drawLine(centerX, centerY, c[0], c[1], paints[index])
                    if (c[0] - centerX < 0) {
                        canvas.drawLine(c[0], c[1], c[0] - 30, c[1], paints[index])
                        val widthPercent = paintTextPercent.measureText("${percent} %")
                        val widthCategory = paintTextCategory.measureText(chartPart.categoryName)
                        val startX = c[0] - 40 - max(widthCategory, widthPercent)
                        canvas.drawText(
                            "$percent %", startX, c[1] - 5, paintTextPercent
                        )
                        canvas.drawText(
                            chartPart.categoryName, startX, c[1] + 20, paintTextCategory
                        )
                    } else {
                        canvas.drawLine(c[0], c[1], c[0] + 30, c[1], paints[index])
                        canvas.drawText(
                            "$percent %", c[0] + 40, c[1] - 5, paintTextPercent
                        )
                        canvas.drawText(
                            chartPart.categoryName, c[0] + 40, c[1] + 20, paintTextCategory
                        )
                    }
                }
            }
            prevAngle += chartPart.percent * 3.6f
        }
    }

    private fun drawInAlphaCircle(canvas: Canvas) {
        var prevAngle = -90f
        if (parts.isEmpty())
            canvas.drawArc(circleOut, -90f, 360f, true, defaultAlphaPaint)
        parts.forEachIndexed { index, chartPart ->
            canvas.drawArc(
                circleCenter,
                prevAngle + 1f,
                chartPart.percent * 3.6f - 1f,
                true,
                alphaPaints[index]
            )
            prevAngle += chartPart.percent * 3.6f
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val dis = sqrt(
                (event.x - centerX) * (event.x - centerX) + (event.y - centerY) * (event.y - centerY)
            )
            if (dis > radius || dis < inRadius2) {
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
                strokeWidth = 2f
                isAntiAlias = true
                color = it.color
            })

            alphaPaints.add(Paint().apply {
                style = Paint.Style.FILL
                strokeWidth = 3f
                isAntiAlias = true
                color = getAlphaColor(it.color)
            })
        }

        invalidate()
    }

    private fun getAlphaColor(color: Int): Int {
        return ((color and 0x00FFFFFF) or (128 shl 24))
    }
}
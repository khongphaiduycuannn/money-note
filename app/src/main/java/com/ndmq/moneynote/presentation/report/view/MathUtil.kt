package com.ndmq.moneynote.presentation.report.view

import kotlin.math.PI
import kotlin.math.sqrt

object MathUtil {

    fun radianToDegree(rad: Float): Float {
        return (rad / PI * 180f).toFloat()
    }

    fun degreeToRadian(degree: Float): Float {
        return (degree / 180f * PI).toFloat()
    }

    fun getC(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        r: Float,
        center: Boolean = false
    ): Array<Float> {
        val vx = x2 - x1
        val vy = y2 - y1
        val len = sqrt(vx * vx + vy * vy)
        return if (!center) arrayOf(
            x1 + r * (x2 - x1) / len,
            y1 + r * (y2 - y1) / len
        ) else arrayOf(
            x1 - r * (x2 - x1) / len,
            y1 - r * (y2 - y1) / len
        )
    }
}
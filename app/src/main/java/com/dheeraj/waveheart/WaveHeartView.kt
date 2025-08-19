package com.dheeraj.waveheart

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


/**
 * Created by Dheeraj Kotwani on 10/08/25
 * Copyright (c) 2025 Oscillating Heart . All rights reserved.
 */
class WaveHeartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 3f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var k = 60.0

    private val animator = ValueAnimator.ofFloat(20f, 40f).apply {
        duration = 3000 // 3 seconds from min to max
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE // goes back down
        interpolator = LinearInterpolator()
        addUpdateListener {
            k = (it.animatedValue as Float).toDouble()
            invalidate() // redraw with new k
        }
        start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val path = Path()
        val centerY = height / 2f
        val centerX = width / 2f
        val scale = min(width, height) / 4f // size scaling

        val steps = 1000
        var first = true

        path.moveTo(0f, height/2f)

        for (i in 0..steps) {
            val t = i / steps.toDouble()
            val x = lerp(-4.0, 4.0, t)

            val rad = 3.0 - x * x
            if (rad < 0) continue

            val y = abs(x).pow(2.0 / 3) + 0.9 * sin(k * x) * sqrt(rad)

            val px = centerX + (x * scale).toFloat()
            val py = centerY - (y * scale).toFloat()

            if (first) {
                path.moveTo(px, py)
                first = false
            } else {
                path.lineTo(px, py)
            }
        }

        canvas.drawPath(path, paint)
    }

    private fun lerp(a: Double, b: Double, t: Double) = a + (b - a) * t
}
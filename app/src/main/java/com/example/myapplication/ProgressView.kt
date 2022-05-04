package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2022/5/4 9:50 下午
 */
class ProgressView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var totalDuration: Long = Long.MAX_VALUE

    var timestamps: Array<Double> = arrayOf()

    var curDuration: Long = 1
        set(value) {
            field = value
            invalidate()
        }

    val bgPaint: Paint by lazy {
        val p = Paint().apply {
            color = Color.parseColor("#112B3C")
        }
        p
    }

    val progressPaint: Paint by lazy {
        val p = Paint().apply {
            color = Color.parseColor("#205375")
        }
        p
    }

    val pointPaint: Paint by lazy {
        val p = Paint().apply {
            color = Color.parseColor("#F66B0E")
            strokeWidth = 4f
        }
        p
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        canvas.drawRect(0f, 0f, width * (curDuration.toFloat() / totalDuration), height.toFloat(), progressPaint)
        for (timestamp in timestamps) {
            canvas.drawPoint(width * (timestamp * 1000f / totalDuration).toFloat(), height.toFloat() / 2, pointPaint)
        }
    }
}
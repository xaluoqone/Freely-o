package com.xaluoqone.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.toColorInt

class EnhanceTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var isPress = false
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    var cornerRadius: Float
    var backgroundValue: Int
    var pressBackgroundValue: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EnhanceTextView)
        cornerRadius = typedArray.getDimension(R.styleable.EnhanceTextView_cornerRadius, 0f)
        backgroundValue = typedArray.getColor(
            R.styleable.EnhanceTextView_backgroundValue,
            "#00000000".toColorInt()
        )
        pressBackgroundValue = typedArray.getColor(
            R.styleable.EnhanceTextView_pressBackgroundValue,
            "#00000000".toColorInt()
        )
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        mPaint.color = if (isPress) pressBackgroundValue else backgroundValue
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            mPaint
        )
        super.onDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (pressBackgroundValue != 0 && backgroundValue != 0) {
                    isPress = true
                }
            }
            MotionEvent.ACTION_UP -> isPress = false
        }
        return true
    }
}
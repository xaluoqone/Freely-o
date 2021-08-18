package com.xaluoqone.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class EnhanceTextView(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var isPress by viewPropertyOf(false)
    var cornerRadius by viewPropertyOf(0f)
    var backgroundValue by viewPropertyOf(0)
    var pressBackgroundValue by viewPropertyOf(0)
    var backgroundStroke by viewPropertyOf(0)
    var pressBackgroundStroke by viewPropertyOf(0)
    var backgroundStyle by viewPropertyOf(Paint.Style.FILL)
    var pressBackgroundStyle by viewPropertyOf(Paint.Style.FILL)
    var textColorValue by viewPropertyOf(0)
    var pressTextColorValue by viewPropertyOf(0)
    var backgroundStrokeWidth by viewPropertyOf(0f)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EnhanceTextView)
        cornerRadius = typedArray.getDimension(R.styleable.EnhanceTextView_cornerRadius, 0f)
        backgroundValue = typedArray.getColor(R.styleable.EnhanceTextView_backgroundValue, 0)
        pressBackgroundValue =
            typedArray.getColor(R.styleable.EnhanceTextView_pressBackgroundValue, 0)
        backgroundStroke = typedArray.getColor(R.styleable.EnhanceTextView_backgroundStroke, 0)
        pressBackgroundStroke =
            typedArray.getColor(R.styleable.EnhanceTextView_pressBackgroundStroke, 0)
        backgroundStyle =
            getStyleByAttrStr(typedArray.getInt(R.styleable.EnhanceTextView_backgroundStyle, 1))
        pressBackgroundStyle =
            getStyleByAttrStr(
                typedArray.getInt(
                    R.styleable.EnhanceTextView_pressBackgroundStyle,
                    1
                )
            )
        textColorValue = typedArray.getColor(R.styleable.EnhanceTextView_textColorValue, 0)
        pressTextColorValue =
            typedArray.getColor(R.styleable.EnhanceTextView_pressTextColorValue, 0)
        backgroundStrokeWidth =
            typedArray.getDimension(R.styleable.EnhanceTextView_backgroundStrokeWidth, 0f)
        typedArray.recycle()
    }

    private fun getStyleByAttrStr(attrStr: Int?) =
        when (attrStr) {
            1 -> Paint.Style.FILL
            2 -> Paint.Style.STROKE
            3 -> Paint.Style.FILL_AND_STROKE
            else -> Paint.Style.FILL
        }

    override fun onDraw(canvas: Canvas) {
        mPaint.style = if (isPress) pressBackgroundStyle else backgroundStyle
        mPaint.strokeWidth = backgroundStrokeWidth
        when (mPaint.style!!) {
            Paint.Style.STROKE -> {
                mPaint.color = if (isPress) pressBackgroundStroke else backgroundStroke
                canvas.drawBg(width, height, mPaint.strokeWidth / 2)
            }
            Paint.Style.FILL -> {
                mPaint.color = if (isPress) pressBackgroundValue else backgroundValue
                canvas.drawBg(width, height, 0f)
            }
            Paint.Style.FILL_AND_STROKE -> {
                val offset = mPaint.strokeWidth / 2
                mPaint.color = if (isPress) pressBackgroundStroke else backgroundStroke
                mPaint.style = Paint.Style.STROKE
                canvas.drawBg(width, height, offset)
                mPaint.color = if (isPress) pressBackgroundValue else backgroundValue
                mPaint.style = Paint.Style.FILL
                canvas.drawBg(width, height, offset)
            }
        }
        super.onDraw(canvas)
    }

    private fun Canvas.drawBg(
        bgWidth: Int,
        bgHeight: Int,
        drawOffset: Float
    ) {
        drawRoundRect(
            drawOffset,
            drawOffset,
            bgWidth - drawOffset,
            bgHeight - drawOffset,
            cornerRadius,
            cornerRadius,
            mPaint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (((pressBackgroundValue != 0 || pressBackgroundStroke != 0) &&
                            (backgroundValue != 0 || backgroundStroke != 0)) ||
                    (pressTextColorValue != 0 && textColorValue != 0)
                ) {
                    if (pressTextColorValue != 0) {
                        setTextColor(pressTextColorValue)
                    }
                    isPress = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (textColorValue != 0) {
                    setTextColor(textColorValue)
                }
                isPress = false
            }
        }
        return true
    }
}
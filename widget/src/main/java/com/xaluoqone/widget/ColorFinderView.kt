package com.xaluoqone.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.imageview.ShapeableImageView
import com.xaluoqone.widget.ex.dp

class ColorFinderView(context: Context, attrs: AttributeSet? = null) :
    ShapeableImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var bitmap: Bitmap

    // 小圆圈相关属性
    private val indicatorRadius = 12.dp
    private val indicatorStrokeWidth = 3.dp
    private val indicatorRawRadius = indicatorRadius + indicatorStrokeWidth
    private var indicatorX by viewPropertyOf(indicatorRawRadius)
    private var indicatorY by viewPropertyOf(indicatorRawRadius)
    private val indicatorStrokeColor by viewPropertyOf(0)
    private var indicatorIsMoving = false

    // 小圆圈移动监听
    private var onIndicatorMoving: ((Int) -> Unit)? = null
    private var onIndicatorMoveFinished: ((Int) -> Unit)? = null

    fun onIndicatorMoving(listener: (Int) -> Unit) {
        onIndicatorMoving = listener
    }

    fun onIndicatorMoveFinished(listener: (Int) -> Unit) {
        onIndicatorMoveFinished = listener
    }

    private lateinit var indicatorBound: RectF

    init {
        adjustViewBounds = true
        paint.strokeWidth = indicatorStrokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = drawable.toBitmap(w, h)
        indicatorBound =
            RectF(
                indicatorRawRadius,
                indicatorRawRadius,
                w - indicatorRawRadius,
                h - indicatorRawRadius
            )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画小圆圈
        paint.style = Paint.Style.FILL
        paint.color = bitmap.getPixel(indicatorX.toInt(), indicatorY.toInt())
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, paint)
        //画小圆圈的边框
        paint.style = Paint.Style.STROKE
        paint.color = indicatorStrokeColor
        canvas.drawCircle(
            indicatorX,
            indicatorY,
            indicatorRadius + (indicatorStrokeWidth / 2),
            paint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                indicatorIsMoving = true
                updateIndicatorPosition(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                indicatorIsMoving = false
                updateIndicatorPosition(event.x, event.y)
            }
        }
        return true
    }

    private fun updateIndicatorPosition(x: Float, y: Float) {
        indicatorX = when {
            x < indicatorBound.left -> indicatorBound.left
            x > indicatorBound.right -> indicatorBound.right
            else -> x
        }
        indicatorY = when {
            y < indicatorBound.top -> indicatorBound.top
            y > indicatorBound.bottom -> indicatorBound.bottom
            else -> y
        }
        callbackListener()
    }

    private fun callbackListener() {
        val color = bitmap.getPixel(indicatorX.toInt(), indicatorY.toInt())
        if (indicatorIsMoving) {
            onIndicatorMoving?.invoke(color)
        }
        else {
            onIndicatorMoveFinished?.invoke(color)
        }
    }
}
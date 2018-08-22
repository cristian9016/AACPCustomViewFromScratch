package com.example.personal.aacpcustomviewfromscratch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View



class DialView : View {

    private var mWidth = 0f
    private var mHeight = 0f
    private lateinit var textPaint: Paint
    private lateinit var dialPaint: Paint
    private var mradius = 0f
    private var activeSelection = 0

    private final var tempLabel = StringBuffer(8)
    private final var tempResult = FloatArray(2)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mradius = (Math.min(mWidth, mHeight) / 2 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Draw the dial.
        canvas!!.drawCircle(mWidth / 2, mHeight / 2, mradius, dialPaint)
// Draw the text labels.
        val lRadius = mradius + 20
        val label = tempLabel
        for (i in 0 until SELECTION_COUNT) {
            val xyData = computeXYForPosition(i, lRadius)
            val x = xyData[0]
            val y = xyData[1]
            label.setLength(0)
            label.append(i)
            canvas!!.drawText(label, 0, label.length, x, y, textPaint)
        }
// Draw the indicator mark.
        val markerRadius = mradius - 35
        val xyData = computeXYForPosition(activeSelection,
                markerRadius)
        val x = xyData[0]
        val y = xyData[1]
        canvas!!.drawCircle(x, y, 20f, textPaint)
    }

    private fun computeXYForPosition(pos: Int, radius: Float): FloatArray {
//        var result = tempResult
//        var startAngle = Math.PI * (9 / 8)
//        var angle = startAngle + (pos * (Math.PI / 4))
//        result[0] = ((radius * Math.cos(angle)) + (mWidth / 2)).toFloat()
//        result[1] = ((radius * Math.sin(angle)) + (mHeight / 2)).toFloat()
//        return result
        val result = tempResult
        val startAngle = Math.PI * (9 / 8.0) // Angles are in radians.
        val angle = startAngle + pos * (Math.PI / 4)
        result[0] = (radius * Math.cos(angle) + (mWidth / 2)).toFloat()
        result[1] = (radius * Math.sin(angle) + (mHeight / 2)).toFloat()
        return result
    }

    private fun init() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 40f
        dialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dialPaint.color = Color.GRAY
        activeSelection = 0

        setOnClickListener {
            activeSelection = (activeSelection+1)% SELECTION_COUNT
            if(activeSelection>=1) dialPaint.color = Color.GREEN
            else dialPaint.color = Color.GRAY
            invalidate()
        }

    }

    companion object {
        const val SELECTION_COUNT = 4
    }
}

package com.wengabytes.movieviewer.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.recyclerview.widget.RecyclerView


class SeatMapRecyclerView : RecyclerView {
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1f
    private var maxWidth = 0.0f
    private var maxHeight = 0.0f
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f
    private var mPosX = 0f
    private var mPosY = 0f
    private var width = 0f
    private var height = 0f

    constructor(context: Context?) : super(context!!) {
        if (!isInEditMode) mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        if (!isInEditMode) mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        if (!isInEditMode) mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (ev.action == MotionEvent.ACTION_DOWN ||
                ev.action == MotionEvent.ACTION_MOVE
            ) {
                val newX = (ev.x + mPosX * -1) / mScaleFactor
                val newY = (ev.y + mPosY * -1) / mScaleFactor

                Log.d("***zoom", mScaleFactor.toString())
                Log.d("***actual", "${ev.x}; ${ev.y}")
                Log.d("***new", "$newX; $newY")

                ev.setLocation(newX, newY)
            }

            mLastTouchX = ev.x
            mLastTouchY = ev.y

            return super.onInterceptTouchEvent(ev)

        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        super.onTouchEvent(ev)

        val action = ev.action
        mScaleDetector!!.onTouchEvent(ev)

        if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
            val x = ev.x
            val y = ev.y
            val dx = x - mLastTouchX
            val dy = y - mLastTouchY

            mPosX += dx
            mPosY += dy

            if (mPosX > 0.0f)
                mPosX = 0.0f
            else if (mPosX < maxWidth)
                mPosX = maxWidth

            if (mPosY > 0.0f)
                mPosY = 0.0f
            else if (mPosY < maxHeight)
                mPosY = maxHeight

            mLastTouchX = x
            mLastTouchY = y

            invalidate()
        }

        return true
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        if (mScaleFactor <= 1.0f) {
            mPosX = 0.0f
            mPosY = 0.0f
        }
        canvas.translate(mPosX, mPosY)
        canvas.scale(mScaleFactor, mScaleFactor)
        super.dispatchDraw(canvas)
        canvas.restore()
        invalidate()
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = 1.0f.coerceAtLeast(mScaleFactor.coerceAtMost(5.0f))
            maxWidth = width - width * mScaleFactor
            maxHeight = height - height * mScaleFactor
            invalidate()
            return true
        }
    }
}
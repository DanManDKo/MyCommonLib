package com.sprinklebit.library.presentation.widget.graph

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewParent
import com.sprinklebit.library.presentation.widget.graph.FixedLineChart

open class TouchedLineChart(context: Context, attrs: AttributeSet?)
    : FixedLineChart(context, attrs),
        GestureDetector.OnGestureListener {

    private val gestureDetector: GestureDetector by lazy { GestureDetector(context, this) }
    private var dragging: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            highlightValue(null, true)
            dragging = false
        }
        gestureDetector.onTouchEvent(event)
        if (dragging) {
            disallowInterceptTouch(parent)
            val h = getHighlightByTouchPoint(event.x, event.y)
            highlightValue(h, true)
        }
        return true
    }

    private fun disallowInterceptTouch(viewParent: ViewParent?) {
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(true)
            disallowInterceptTouch(viewParent.parent)
        }
    }

    override fun onDown(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(motionEvent: MotionEvent) {

    }

    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(motionEvent: MotionEvent, motionEvent1: MotionEvent, v: Float, v1: Float)
            : Boolean {
        return false
    }

    override fun onLongPress(motionEvent: MotionEvent) {
        dragging = true
        val h = getHighlightByTouchPoint(motionEvent.x, motionEvent.y)
        highlightValue(h, true)
    }

    override fun onFling(motionEvent: MotionEvent, motionEvent1: MotionEvent, v: Float, v1: Float)
            : Boolean {
        return false
    }

}

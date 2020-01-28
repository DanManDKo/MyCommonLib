package com.sprinklebit.library.presentation.widget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewParent
import android.widget.FrameLayout

class SwipeLockedFrameLayout(context: Context, attrs: AttributeSet)
    : FrameLayout(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        notifyAllParentsExceptRecyclerDoNotInterceptSwipe(parent)
        return super.dispatchTouchEvent(ev)
    }

    private fun notifyAllParentsExceptRecyclerDoNotInterceptSwipe(view: ViewParent) {
        if (view.parent != null) {
            notifyAllParentsExceptRecyclerDoNotInterceptSwipe(view.parent)
        }
        if (view !is RecyclerView) {
            view.requestDisallowInterceptTouchEvent(true)
        } else {
            view.requestDisallowInterceptTouchEvent(false)
        }
    }
}
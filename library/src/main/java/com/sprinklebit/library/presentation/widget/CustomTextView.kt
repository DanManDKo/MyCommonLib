package com.sprinklebit.library.presentation.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.sprinklebit.library.R


/**
 * Created with Android Studio.
 * User: Danil Konovalenko
 * Date: 7/18/18
 * Time: 11:43 AM
 */
class CustomTextView : AppCompatTextView {

    @ColorInt
    private var tintColor = Color.TRANSPARENT

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomTextView)
            tintColor = typedArray.getColor(
                    R.styleable.CustomTextView_ctv_drawableTint,
                    Color.TRANSPARENT)

            updateTint()
            typedArray.recycle()
        }
    }

    private fun updateTint() {
        if (tintColor != Color.TRANSPARENT) {
            var drawables = compoundDrawables
            if (drawables.all { it == null }) drawables = compoundDrawablesRelative
            if (drawables.size != DRAWABLES_LENGTH) return

            val wrappedDrawables = arrayOfNulls<Drawable>(DRAWABLES_LENGTH)
            for (i in 0 until DRAWABLES_LENGTH) {
                val drawable = drawables[i]
                if (drawable != null) {
                    val wrappedDrawable = DrawableCompat.wrap(drawable).mutate()
                    DrawableCompat.setTint(wrappedDrawable, tintColor)
                    wrappedDrawables[i] = wrappedDrawable
                }
            }
            setCompoundDrawablesWithIntrinsicBounds(wrappedDrawables[DRAWABLE_LEFT_POSITION],
                    wrappedDrawables[DRAWABLE_TOP_POSITION],
                    wrappedDrawables[DRAWABLE_RIGHT_POSITION],
                    wrappedDrawables[DRAWABLE_BOTTOM_POSITION])
        }
    }

    companion object {

        private const val DRAWABLE_LEFT_POSITION = 0
        private const val DRAWABLE_TOP_POSITION = 1
        private const val DRAWABLE_RIGHT_POSITION = 2
        private const val DRAWABLE_BOTTOM_POSITION = 3
        private const val DRAWABLES_LENGTH = 4
    }

}
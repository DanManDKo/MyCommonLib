package com.sprinklebit.library.presentation.common


import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * Drawable decorator which draws the target drawable similarly to an ImageView with scaleType=centerCrop
 *
 * Example usage:
 * final Drawable bg = getResources().getDrawable(R.drawable.screen);
 * getWindow().setBackgroundDrawable(new CenterCropDrawable(bg));
 */
class CenterCropDrawable : Drawable {

    private var target: Drawable?
    private val consState: ConstantState

    constructor(context: Context, @DrawableRes resId: Int) : super() {
        this.target = ContextCompat.getDrawable(context, resId)

        this.consState = object : ConstantState() {

            override fun getChangingConfigurations(): Int {
                return this@CenterCropDrawable.changingConfigurations
            }

            override fun newDrawable(): Drawable {
                return CenterCropDrawable(context, resId)
            }
        }
    }

    constructor(drawable: Drawable) : super() {
        this.target = drawable

        this.consState = object : ConstantState() {

            override fun getChangingConfigurations(): Int {
                return this@CenterCropDrawable.changingConfigurations
            }

            override fun newDrawable(): Drawable {
                return CenterCropDrawable(drawable)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        try {
            if (target == null) return
            canvas.save()
            canvas.clipRect(bounds)
            target!!.draw(canvas)
            canvas.restore()
        } catch(e: Throwable) {
            Timber.e(e)
        }
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        if (target == null) return
        val sourceRect = RectF(0f, 0f,
                target!!.intrinsicWidth.toFloat(),
                target!!.intrinsicHeight.toFloat())
        val screenRect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

        val matrix = Matrix()
        matrix.setRectToRect(screenRect, sourceRect, Matrix.ScaleToFit.CENTER)

        val inverse = Matrix()
        matrix.invert(inverse)
        inverse.mapRect(sourceRect)

        target!!.setBounds(Math.round(sourceRect.left), Math.round(sourceRect.top),
                Math.round(sourceRect.right), Math.round(sourceRect.bottom))

        super.setBounds(left, top, right, bottom)
    }

    override fun getConstantState(): ConstantState {
        return consState
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) {
        target?.let { it.alpha = alpha}
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        target!!.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return target!!.opacity
    }
}

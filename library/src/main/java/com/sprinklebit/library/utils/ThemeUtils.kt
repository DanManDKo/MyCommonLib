package com.sprinklebit.library.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable

object ThemeUtils {

    @JvmStatic
    fun getColorFromAttrRes(context: Context, attr: Int): Int {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColor(0, 0)
        } finally {
            a.recycle()
        }
    }

    @JvmStatic
    fun getDrawableFromRes(context: Context, attr: Int): Drawable? {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getDrawable(0)
        } finally {
            a.recycle()
        }
    }

    @JvmStatic
    fun getColorStateList(context: Context, attr: Int): ColorStateList? {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColorStateList(0)
        } finally {
            a.recycle()
        }
    }
}

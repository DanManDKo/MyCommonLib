package com.sprinklebit.library.utils

import android.content.Context
import android.content.res.ColorStateList

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

    fun getColorStateList(context: Context, attr: Int): ColorStateList? {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColorStateList(0)
        } finally {
            a.recycle()
        }
    }
}

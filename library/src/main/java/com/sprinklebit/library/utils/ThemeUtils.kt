package com.sprinklebit.library.utils

import android.content.Context

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
}

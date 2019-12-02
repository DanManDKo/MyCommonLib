@file:Suppress("unused")

package com.sprinklebit.library.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable

object ThemeUtils {

    @JvmStatic
    fun getColorFromAttr(context: Context, attr: Int): Int {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColor(0, 0)
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

    fun getColorFromAttr(theme: Resources.Theme, attr: Int): Int {
        val a = theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColor(0, 0)
        } finally {
            a.recycle()
        }
    }

    fun getColorStateListFromAttr(theme: Resources.Theme, attr: Int): ColorStateList? {
        val a = theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColorStateList(0)
        } finally {
            a.recycle()
        }
    }

    @JvmStatic
    fun getDrawableFromAttr(context: Context, attr: Int): Drawable? {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getDrawable(0)
        } finally {
            a.recycle()
        }
    }


    @JvmStatic
    fun getDrawableFromAttr(theme: Resources.Theme, attr: Int): Drawable? {
        val a = theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getDrawable(0)
        } finally {
            a.recycle()
        }
    }
}

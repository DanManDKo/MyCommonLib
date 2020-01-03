package com.sprinklebit.library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 10/3/17
 * Time: 7:10 PM
 */

object SpannableUtils {

    @JvmStatic
    fun doSizeProportionSpan(spanStr: SpannableString, string: String, proportion: Float) {
        val start = spanStr.toString().indexOf(string)
        spanStr.setSpan(RelativeSizeSpan(proportion),
                start, start + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    @JvmStatic
    @SuppressLint("ResourceAsColor")
    fun doAttrColorSpan(context: Context,
                        spanStr: SpannableString,
                        string: String,
                        @AttrRes resColor: Int) {
        val attrs = intArrayOf(resColor)
        val ta = context.obtainStyledAttributes(attrs)
        val color = ta.getColor(0, android.R.color.black)
        ta.recycle()

        val start = spanStr.toString().indexOf(string)
        spanStr.setSpan(ForegroundColorSpan(color),
                start, start + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    @JvmStatic
    fun doColorSpan(context: Context,
                    spanStr: SpannableString,
                    string: String,
                    @ColorRes resColor: Int) {
        val start = spanStr.toString().indexOf(string)
        spanStr.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, resColor)),
                start, start + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    @JvmStatic
    fun doItalicSpan(context: Context,
                     spanStr: SpannableString,
                     string: String,
                     @ColorRes resColor: Int){
        val start = spanStr.toString().indexOf(string)
        spanStr.setSpan(StyleSpan(Typeface.ITALIC), start, start + string.length, 0)
        spanStr.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, resColor)),
                start, start + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    @JvmStatic
    fun doBoldSpan(context: Context,
                   spanStr: SpannableString,
                   string: String,
                   @AttrRes resColor: Int) {
        val start = spanStr.toString().indexOf(string)
        val color = ThemeUtils.getColorFromAttr(context, resColor)
        spanStr.setSpan(StyleSpan(Typeface.BOLD), start, start + string.length, 0)
        spanStr.setSpan(ForegroundColorSpan(color),
                start, start + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

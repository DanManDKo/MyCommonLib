package com.movies.popular.popularmovies.presentation.common.binding

import android.databinding.BindingAdapter
import android.text.Html
import android.widget.TextView

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/1/17
 * Time: 12:32 PM
 */

object NumberBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:bind_tv_html"], requireAll = false)
    fun formatHtmlText(textView: TextView, value: String?) {
        if (value == null || value.isEmpty()) {
            return
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(value)
        }
    }

}

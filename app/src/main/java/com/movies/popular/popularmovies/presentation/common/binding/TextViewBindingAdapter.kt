package com.movies.popular.popularmovies.presentation.common.binding

import androidx.databinding.BindingAdapter
import android.text.Html
import android.widget.TextView

/**
 * User: Sasha Shcherbinin
 * Date : 7/10/18
 */
object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:bind_htmlText"], requireAll = false)
    fun formatHtmlText(textView: TextView, value: String) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(value)
        }
    }
}

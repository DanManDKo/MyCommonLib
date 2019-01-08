package com.movies.popular.popularmovies.presentation.common.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 3/29/18
 * Time: 2:09 PM
 */
object EditTextBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:bind_text_input_error"])
    fun bindError(view: TextInputLayout, string: String?) {
        view.error = string
    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_text_input_hint"])
    fun bindHint(view: TextInputLayout, string: String?) {
        view.hint = string
    }
}

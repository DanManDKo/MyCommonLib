package com.movies.popular.popularmovies.presentation.common.binding

import androidx.databinding.BindingAdapter
import androidx.core.view.ViewCompat
import android.view.View

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 10/29/17
 * Time: 2:09 PM
 */

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:bind_view_elevation"], requireAll = false)
    fun formatPrice(view: View, value: Float) {
        view.post { ViewCompat.setElevation(view, value) }
    }

}

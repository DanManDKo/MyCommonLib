package com.movies.popular.popularmovies.presentation.common.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.SearchView

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/1/17
 * Time: 12:32 PM
 */

object SearchViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:sv_onQueryTextChange"], requireAll = false)
    fun formatPrice(searchView: SearchView, textChangeListener: TextChangeListener) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                textChangeListener.textChange(newText)
                return true
            }
        })
    }

    interface TextChangeListener {
        fun textChange(newText: String)
    }
}

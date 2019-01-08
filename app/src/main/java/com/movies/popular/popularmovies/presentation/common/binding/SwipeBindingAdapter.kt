package com.movies.popular.popularmovies.presentation.common.binding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/1/17
 * Time: 12:32 PM
 */
object SwipeBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:srl_onRefresh"], requireAll = false)
    fun onRefresh(swipe: androidx.swiperefreshlayout.widget.SwipeRefreshLayout, listener: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener) {
        swipe.setOnRefreshListener(listener)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:srl_setRefreshing"], requireAll = false)
    fun setRefresing(swipe: androidx.swiperefreshlayout.widget.SwipeRefreshLayout, refreshing: Boolean) {
        swipe.isRefreshing = refreshing
    }

}

package com.movies.popular.popularmovies.presentation.common.binding

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/1/17
 * Time: 12:32 PM
 */
object SwipeBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:srl_onRefresh"], requireAll = false)
    fun onRefresh(swipe: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
        swipe.setOnRefreshListener(listener)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:srl_setRefreshing"], requireAll = false)
    fun setRefresing(swipe: SwipeRefreshLayout, refreshing: Boolean) {
        swipe.isRefreshing = refreshing
    }

}

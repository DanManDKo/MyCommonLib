package com.movies.popular.popularmovies.presentation.common.binding

import android.databinding.BindingAdapter
import android.support.design.widget.TabLayout

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/1/17
 * Time: 12:32 PMx
 */

object TabBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:bind_onTabSelected"], requireAll = false)
    fun formatPrice(tabLayout: TabLayout, action: Action) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                action.act(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_selectedPosition"], requireAll = false)
    fun formatPrice(tabLayout: TabLayout, position: Int) {
        val tab = tabLayout.getTabAt(position)!!
        tab.select()
    }

    interface Action {
        fun act(position: Int)
    }
}

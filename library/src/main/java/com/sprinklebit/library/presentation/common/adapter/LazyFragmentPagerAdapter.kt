package com.sprinklebit.library.presentation.common.adapter

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter
import timber.log.Timber

/**
 * User: Sasha Shcherbinin
 * Date : 2/7/19
 */
abstract class LazyFragmentPagerAdapter(private val fragmentManager: FragmentManager)
    : PagerAdapter() {

    private var curTransaction: FragmentTransaction? = null
    private var currentPrimaryItem: Fragment? = null

    /**
     * Return the Fragment associated with a specified position.
     */
    abstract fun getItem(position: Int): Fragment

    override fun startUpdate(container: ViewGroup) {
        if (container.id == View.NO_ID) {
            throw IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (curTransaction == null) {
            curTransaction = this.fragmentManager.beginTransaction()
        }

        val itemId = getItemId(position)

        // Do we already have this fragment?
        val name = makeFragmentName(container.id, itemId)
        var fragment = this.fragmentManager.findFragmentByTag(name)
        if (fragment != null) {
            Timber.v("Attaching item #$itemId: f=$fragment")
            curTransaction!!.attach(fragment)

            if (fragment !== currentPrimaryItem) {
                fragment.setMenuVisibility(false)
                fragment.userVisibleHint = false
            }
        } else {
            fragment = getItem(position)
        }

        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        if (curTransaction == null) {
            curTransaction = this.fragmentManager.beginTransaction()
        }

        curTransaction!!.detach(any as Fragment)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
        val fragment: Fragment = any as Fragment
        val tagName = makeFragmentName(container.id, getItemId(position))
        if (fragmentManager.findFragmentByTag(tagName) == null) {
            if (curTransaction == null) {
                curTransaction = this.fragmentManager.beginTransaction()
            }
            curTransaction!!.add(container.id, fragment, tagName)
        }
        if (fragment !== currentPrimaryItem) {
            if (currentPrimaryItem != null) {
                currentPrimaryItem!!.setMenuVisibility(false)
                currentPrimaryItem!!.userVisibleHint = false
            }
            fragment.setMenuVisibility(true)
            fragment.userVisibleHint = true
            currentPrimaryItem = fragment
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        if (curTransaction != null) {
            curTransaction!!.commitNowAllowingStateLoss()
            curTransaction = null
        }
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return (any as Fragment).view === view
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    /**
     * Return a unique identifier for the item at the given position.
     *
     *
     * The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun makeFragmentName(viewId: Int, id: Long): String {
        return "android:switcher:$viewId:$id"
    }
}

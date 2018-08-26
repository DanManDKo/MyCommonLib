package com.sprinklebit.library.presentation.common

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView


/**
 * User: Sasha Shcherbinin
 * Date : 8/27/18
 */
abstract class FixedPagedListAdapter<T, VH : RecyclerView.ViewHolder>
constructor(private val diffCallback: DiffUtil.ItemCallback<T>) : PagedListAdapter<T, VH>(diffCallback) {

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun submitList(pagedList: PagedList<T>?) {
        super.submitList(pagedList)
        if (currentList != null &&
                pagedList != null &&
                currentList!!.size >= pagedList.size - 1 &&
                pagedList.isNotEmpty() &&
                diffCallback.areItemsTheSame(currentList!![pagedList.size - 1], pagedList[pagedList.size - 1])) {
            recyclerView.postDelayed({
                notifyItemChanged(pagedList.size - 1)
            }, 1000)
        }
    }
}

package com.sprinklebit.library.presentation.common

import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


/**
 * User: Sasha Shcherbinin
 * Date : 8/27/18
 */
abstract class FixedPagedListAdapter<T, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder>
constructor(private val diffCallback: DiffUtil.ItemCallback<T>) : PagedListAdapter<T, VH>(diffCallback) {

    private var lastPos = 0

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun submitList(pagedList: PagedList<T>?) {
        super.submitList(pagedList)
        if (currentList != null &&
                pagedList != null &&
                currentList!!.size >= pagedList.size &&
                pagedList.isNotEmpty() &&
                diffCallback.areItemsTheSame(currentList!![pagedList.size - 1]!!, pagedList[pagedList.size - 1]!!)
                && pagedList.size <= recyclerView.childCount) {
            pagedList.loadAround(pagedList.size - 1)
        }
    }

}

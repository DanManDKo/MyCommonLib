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
                areItemsTheSame(pagedList) &&
                pagedList.size <= recyclerView.childCount) {
            pagedList.loadAround(pagedList.size - 1)
        }
    }

    private fun areItemsTheSame(pagedList: PagedList<T>): Boolean {
        val oldItem = currentList!![pagedList.size - 1]
        val newItem = pagedList[pagedList.size - 1]

        if (oldItem != null && newItem != null) {
            return diffCallback.areItemsTheSame(oldItem, newItem)
        }

        if (oldItem == null && newItem == null) {
            return true
        }

        if (oldItem == null && newItem != null) {
            return false
        }

        if (oldItem != null && newItem == null) {
            return false
        }
        throw IllegalArgumentException("Unexpected items state old:$oldItem, new:$newItem")
    }

}

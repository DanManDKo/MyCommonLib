package com.sprinklebit.library.presentation.common

import android.annotation.SuppressLint
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.support.annotation.CallSuper
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView


/**
 * User: Sasha Shcherbinin
 * Date : 8/27/18
 */
abstract class FixedPagedListAdapter<T, VH : RecyclerView.ViewHolder>
constructor(diffCallback: DiffUtil.ItemCallback<T>) : PagedListAdapter<T, VH>(diffCallback) {

    private var lastPos = 0

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    @CallSuper
    override fun onBindViewHolder(holder: VH, @SuppressLint("RecyclerView") position: Int) {
        lastPos = position
    }

    override fun onCurrentListChanged(currentList: PagedList<T>?) {
        super.onCurrentListChanged(currentList)
        getItem(Math.min(lastPos, itemCount - 1))
    }

}

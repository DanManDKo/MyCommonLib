package com.sprinklebit.library.presentation.common.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * User: Sasha Shcherbinin
 * Date : 2019-06-16
 */
abstract class ListAdapter<T, VH : RecyclerView.ViewHolder>
constructor(private val diffCallback: DiffUtil.ItemCallback<T>)
    : RecyclerView.Adapter<VH>() {

    var data: DiffData<T>? = null

    fun submitList(list: List<T>?) {
        data!!.updateData(list ?: Collections.emptyList())
    }

    override fun getItemCount(): Int {
        return data!!.size()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        data = DiffData(recyclerView, this, diffCallback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        data = null
    }

    fun getItem(position: Int) : T {
        return data!![position]
    }
}
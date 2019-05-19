package com.sprinklebit.library.presentation.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EmptyAdapter
    : RecyclerViewAdapterWrapper(EmptyAdapter()) {

    override fun getAdapterDataObserver(): RecyclerView.AdapterDataObserver {
        return object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        }
    }

    class EmptyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            throw IllegalStateException()
        }

        override fun getItemCount(): Int {
            return 0
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            throw IllegalStateException()
        }

    }
}

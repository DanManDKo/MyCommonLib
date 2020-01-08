package com.sprinklebit.library.presentation.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sprinklebit.library.R
import timber.log.Timber

@Suppress("unused")
class LoadingAdapter(adapter: RecyclerView.Adapter<*>,
                     private val threshold: Int = 5,
                     loadMoreListener: (() -> Any)? = null)
    : RecyclerViewAdapterWrapper(adapter) {

    private var loadMoreListener: LoadMoreListener? = null
    private var isHorizontal = false

    private var firstTime = true
    private var loading = false
    private var hasNext = true

    private var oldSize = 0

    private val loadingPosition: Int
        get() = wrappedAdapter.itemCount

    companion object {
        const val LOADING_TYPE = -1023
    }

    init {
        loadMoreListener?.let {
            this.loadMoreListener = object : LoadMoreListener {
                override fun onLoadMore() {
                    it.invoke()
                }
            }
        }
    }

    override fun getAdapterDataObserver(): RecyclerView.AdapterDataObserver {
        return object : RecyclerView.AdapterDataObserver() {

            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (oldSize == 0 && hasNext) {
                    notifyItemRemoved(loadingPosition)
                    notifyItemRangeInserted(positionStart, itemCount + 1)
                } else {
                    notifyItemRangeInserted(positionStart, itemCount)
                }
                oldSize = wrappedAdapter.itemCount
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
                oldSize = wrappedAdapter.itemCount
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING_TYPE) {
            val view = when (isHorizontal) {
                true -> LayoutInflater.from(parent.context).inflate(
                        R.layout.item_loading_horizontal, parent, false)
                false -> LayoutInflater.from(parent.context).inflate(
                        R.layout.item_loading_vertical, parent, false)
            }
            LoadingViewHolder(view)
        } else {
            wrappedAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is LoadingViewHolder) {
            wrappedAdapter.onBindViewHolder(holder, position)
        }
        if (!firstTime &&
                !loading &&
                hasNext &&
                loadingPosition - threshold <= position) {
            loadMore()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingPosition(position)) {
            LOADING_TYPE
        } else {
            wrappedAdapter.getItemViewType(position)
        }
    }

    private fun hasWrappedItems(): Boolean {
        return wrappedAdapter.itemCount > 0
    }

    override fun getItemCount(): Int {
        val itemCount = wrappedAdapter.itemCount
        return if (hasNext) itemCount + 1 else itemCount
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder !is LoadingViewHolder) {
            wrappedAdapter.onViewRecycled(holder)
        }
    }

    private fun loadMore() {
        loading = true
        loadMoreListener?.onLoadMore()
    }

    private fun isLoadingPosition(position: Int): Boolean {
        return hasNext && position == loadingPosition
    }

    @Deprecated("Use constructor")
    fun setLoadMoreListener(loadMoreListener: LoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        isHorizontal = recyclerView.layoutManager!!.canScrollHorizontally()
    }

    @Deprecated("use setLoading and setHasNext")
    fun updateLoading(hasNext: Boolean) {
        firstTime = false
        try {
            if (hasNext) {
                resetLoading()
            } else {
                setFinished()
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    private fun setFinished() {
        if (!hasNext) {
            return
        }
        hasNext = false
        loading = false
        notifyItemRemoved(itemCount)
    }

    private fun resetLoading() {
        val add = !hasNext
        loading = false
        hasNext = true
        if (add) {
            notifyItemInserted(loadingPosition)
        } else {
            notifyItemChanged(loadingPosition)
        }
    }

    @Suppress("unused")
    fun setLoading(loading: Boolean) {
        this.loading = loading
    }

    @Suppress("unused")
    fun setHasNext(hasNext: Boolean) {
        firstTime = false
        if (this.hasNext == hasNext && !hasNext) return
        if (hasNext) {
            val add = !this.hasNext
            this.hasNext = hasNext

            if (add) {
                notifyItemInserted(loadingPosition)
            } else {
                notifyItemChanged(loadingPosition)
            }
        } else {
            this.hasNext = hasNext
            notifyItemRemoved(loadingPosition)
        }
    }

    @Suppress("unused")
    fun reset() {
        firstTime = true
        loading = false
        hasNext = true
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

    private inner class LoadingViewHolder internal constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView)

}
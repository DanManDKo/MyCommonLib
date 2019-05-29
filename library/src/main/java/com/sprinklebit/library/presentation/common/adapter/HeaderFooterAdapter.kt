package com.sprinkle.brokerage.presentation.feature.dashboard.portfolio.info

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sprinklebit.library.presentation.common.adapter.RecyclerViewAdapterWrapper

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 11/15/17
 * Time: 6:46 PM
 */
class HeaderFooterAdapter(targetAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>)
    : RecyclerViewAdapterWrapper(targetAdapter) {

    private var layoutManager: RecyclerView.LayoutManager? = null

    private val headerViewDelegates = SparseArray<ViewDelegate<ViewHolder>>(5)
    private var footerViewDelegate: ViewDelegate<ViewHolder>? = null
    private val viewDelegateMap = HashMap<ViewHolder, ViewDelegate<ViewHolder>>()

    private val headerSize: Int
        get() = headerViewDelegates.size()

    override fun getAdapterDataObserver(): RecyclerView.AdapterDataObserver {
        return object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                val i = if (hasHeaders()) headerViewDelegates.size() else 0
                notifyItemRangeChanged(positionStart + i, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val i = if (hasHeaders()) headerViewDelegates.size() else 0
                notifyItemRangeInserted(positionStart + i, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                val i = if (hasHeaders()) headerViewDelegates.size() else 0
                notifyItemRangeRemoved(positionStart + i, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                val i = if (hasHeaders()) headerViewDelegates.size() else 0
                notifyItemMoved(fromPosition + i, toPosition)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun appendHeader(position: Int, viewDelegate: ViewDelegate<*>) {
        val index = headerViewDelegates.indexOfKey(position)
        headerViewDelegates.append(position, viewDelegate as ViewDelegate<ViewHolder>)
        viewDelegate.setAdapter(this)
        if (index >= 0) {
            notifyItemChanged(index)
        } else {
            notifyItemInserted(headerViewDelegates.indexOfKey(position))
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun removeHeaderView(viewDelegate: ViewDelegate<*>) {
        viewDelegate.setAdapter(null)

        val indexOf = headerViewDelegates.indexOfValue(viewDelegate as ViewDelegate<ViewHolder>)
        if (indexOf >= 0) {
            removeHeaderView(headerViewDelegates.keyAt(indexOf))
        }
    }

    fun removeHeaderView(position: Int) {
        val value = headerViewDelegates.get(position)
        if (value != null) {
            value.setAdapter(null)
            headerViewDelegates.remove(position)
            notifyItemRemoved(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setFooter(viewDelegate: ViewDelegate<*>) {
        val hadView = footerViewDelegate != null
        footerViewDelegate = viewDelegate as ViewDelegate<ViewHolder>
        viewDelegate.setAdapter(this)
        if (hadView) {
            notifyItemChanged(getFooterPosition())
        } else {
            notifyItemInserted(getFooterPosition())
        }
    }

    private fun getFooterPosition() = super.getItemCount() + headerSize

    fun removeFooterView() {
        if (footerViewDelegate != null) {
            footerViewDelegate!!.setAdapter(null)
            footerViewDelegate = null
            notifyItemRemoved(getFooterPosition())
        }
    }

    private fun setGridHeaderFooter(layoutManager: RecyclerView.LayoutManager?) {
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager as GridLayoutManager?
            gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val isShowHeader = position < headerSize && hasHeaders()
                    val isShowFooter = position == itemCount - 1 && hasFooter()
                    return if (isShowFooter || isShowHeader) {
                        gridLayoutManager.spanCount
                    } else 1
                }
            }
        }
    }

    private fun hasHeaders(): Boolean {
        return headerViewDelegates.size() != 0
    }

    private fun hasFooter(): Boolean {
        return footerViewDelegate != null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutManager = recyclerView.layoutManager
        setGridHeaderFooter(layoutManager)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + headerSize + if (hasFooter()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (hasHeaders() && position < headerSize) {
            return headerViewDelegates.get(position).getItemViewType()
        }

        return if (hasFooter() && position == getFooterPosition()) {
            return footerViewDelegate!!.getItemViewType()
        } else {
            super.getItemViewType(position - headerSize)
        }
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val viewDelegate = getViewDelegateByViewType(viewType)
        if (viewDelegate != null) {
            viewHolder = viewDelegate.onCreateHolder(parent)
        }

        if (viewHolder != null) {
            //set StaggeredGridLayoutManager header & footer view
            if (layoutManager is StaggeredGridLayoutManager) {
                val targetParams = viewHolder.itemView.layoutParams
                val staggerLayoutParams: StaggeredGridLayoutManager.LayoutParams
                staggerLayoutParams = if (targetParams != null) {
                    StaggeredGridLayoutManager
                            .LayoutParams(targetParams.width, targetParams.height)
                } else {
                    StaggeredGridLayoutManager.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                staggerLayoutParams.isFullSpan = true
                viewHolder.itemView.layoutParams = staggerLayoutParams
            }

            return viewHolder
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    private fun getViewDelegateByViewType(viewType: Int): ViewDelegate<ViewHolder>? {
        for (i in 0 until headerViewDelegates.size()) {
            val valueAt = headerViewDelegates.valueAt(i)
            if (valueAt.getItemViewType() == viewType) return valueAt
        }
        if (footerViewDelegate != null) {
            if (footerViewDelegate!!.getItemViewType() == viewType) return footerViewDelegate
        }
        return null
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val viewDelegate = viewDelegateMap.remove(holder)
        viewDelegate?.onUnbindAdapterView(holder as ViewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < headerSize) {
            val viewDelegate = headerViewDelegates[position]
            viewDelegate.onBindAdapterView(holder as ViewHolder)
            viewDelegateMap[holder] = viewDelegate
        } else if (hasFooter() && getFooterPosition() == position) {
            footerViewDelegate!!.onBindAdapterView(holder as ViewHolder)
            viewDelegateMap[holder] = footerViewDelegate!!
        } else {
            super.onBindViewHolder(holder, if (hasHeaders()) position - headerSize else position)
        }
    }

    abstract class ViewDelegate<Holder : ViewHolder> {

        private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

        fun onCreateHolderInternal(parent: ViewGroup): Holder {
            val onCreateHolder = onCreateHolder(parent)
            onCreateHolder::class.java
            return onCreateHolder
        }

        abstract fun onCreateHolder(parent: ViewGroup): Holder

        abstract fun getItemViewType(): Int

        @CallSuper
        open fun onBindAdapterView(holder: Holder) {
            holder.onBind()
        }

        @CallSuper
        open fun onUnbindAdapterView(holder: Holder) {
            holder.onUnbind()
        }

        fun notifyDataSetChanged() {
            adapter?.notifyDataSetChanged()
        }

        fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
            this.adapter = adapter
        }
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleOwner {

        private var lifecycle: LifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycle
        }

        fun onBind() {
            lifecycle.markState(Lifecycle.State.RESUMED)
        }

        fun onUnbind() {
            lifecycle.markState(Lifecycle.State.DESTROYED)
        }
    }
}

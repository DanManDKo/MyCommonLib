package com.movies.popular.popularmovies.presentation.common.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 10/31/17
 */
class DiffData<Type>(private val recyclerView: androidx.recyclerview.widget.RecyclerView,
                     private val adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>,
                     private val isItemTheSame: (Type, Type) -> Boolean) {

    private val data = ArrayList<Type>()

    fun updateData(newData: List<Type>) {
        val copyData = ArrayList(newData)
        if (data.size > 0 && copyData.size > 1) {
            var clipTop = true
            if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
                val layoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
                clipTop = !layoutManager.reverseLayout
            }
            val offset = recyclerView.computeVerticalScrollOffset()

            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return data.size
                }

                override fun getNewListSize(): Int {
                    return copyData.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return isItemTheSame.invoke(
                            data[oldItemPosition],
                            copyData[newItemPosition])
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val equals = data[oldItemPosition] == copyData[newItemPosition]
                    if (!equals) {
                        Timber.d("are not contents the same")
                    }
                    return equals
                }
            }, false)

            data.clear()
            data.addAll(copyData)
            diffResult.dispatchUpdatesTo(adapter)

            if (clipTop && offset == 0) {
                recyclerView.scrollToPosition(0)
            }
        } else {
            data.clear()
            data.addAll(copyData)
            adapter.notifyDataSetChanged()
        }
    }

    operator fun get(position: Int): Type {
        return data[position]
    }

    fun size(): Int {
        return data.size
    }
}

package com.sprinklebit.library.presentation.common

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 10/31/17
 */
@Deprecated("Use ListAdapter instead")
class DiffData<Type>(private val recyclerView: RecyclerView,
                     private val adapter: RecyclerView.Adapter<*>,
                     private val isItemTheSame: (Type, Type) -> Boolean) {

    private var data: List<Type> = Collections.emptyList()

    private var mainThreadExecutor: Executor = MainThreadExecutor()
    private var diffExecutor = Executors.newFixedThreadPool(2)

    private var mMaxScheduledGeneration: Int = 0

    fun updateData(newData: List<Type>) {
        if (data.isNotEmpty() && newData.size > 1) {
            val oldData = data
            var clipTop = true
            if (recyclerView.layoutManager is LinearLayoutManager) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                clipTop = !layoutManager.reverseLayout
            }
            val offset = recyclerView.computeVerticalScrollOffset()

            val runGeneration = ++mMaxScheduledGeneration

            diffExecutor.execute {
                val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int {
                        return data.size
                    }

                    override fun getNewListSize(): Int {
                        return newData.size
                    }

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return try {
                            isItemTheSame.invoke(
                                    oldData[oldItemPosition],
                                    newData[newItemPosition])
                        } catch (e: Throwable) {
                            false
                        }
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return try {
                            oldData[oldItemPosition] == newData[newItemPosition]
                        } catch (e: Throwable) {
                            false
                        }
                    }
                }, false)
                mainThreadExecutor.execute {
                    if (mMaxScheduledGeneration == runGeneration) {
                        data = ArrayList(newData)
                        diffResult.dispatchUpdatesTo(adapter)
                        if (clipTop && offset == 0) {
                            recyclerView.scrollToPosition(0)
                        }
                    }
                }
            }
        } else {
            data = ArrayList(newData)
            adapter.notifyDataSetChanged()
        }
    }

    operator fun get(position: Int): Type {
        return data[position]
    }

    fun size(): Int {
        return data.size
    }

    private class MainThreadExecutor : Executor {
        internal val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }
}

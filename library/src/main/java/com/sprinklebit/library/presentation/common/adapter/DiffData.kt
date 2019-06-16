package com.sprinklebit.library.presentation.common.adapter

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
internal class DiffData<Type>(private val recyclerView: RecyclerView,
                              private val adapter: RecyclerView.Adapter<*>,
                              private val diffCallback: DiffUtil.ItemCallback<Type>) {

    private var data: List<Type> = Collections.emptyList()

    private var mainThreadExecutor: Executor = MainThreadExecutor()
    private var diffExecutor = Executors.newFixedThreadPool(2)

    private var mMaxScheduledGeneration: Int = 0

    fun updateData(newData: List<Type>) {
        if (data.isNotEmpty() && newData.size > 1) {
            val oldData = data
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
                            diffCallback.areItemsTheSame(
                                    oldData[oldItemPosition],
                                    newData[newItemPosition])
                        } catch (e: Throwable) {
                            false
                        }
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return try {
                            diffCallback.areContentsTheSame(
                                    oldData[oldItemPosition],
                                    newData[newItemPosition])
                        } catch (e: Throwable) {
                            false
                        }
                    }
                }, false)
                mainThreadExecutor.execute {
                    if (mMaxScheduledGeneration == runGeneration) {
                        data = ArrayList(newData)
                        diffResult.dispatchUpdatesTo(adapter)
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

package com.sprinklebit.library.data.common.cashe

import java.util.*

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 10/17/17
 * Time: 4:39 PM
 */
class Page<T>(var hasNext: Boolean = false) {

    val dataList: MutableList<T> = Collections.synchronizedList(ArrayList())
    val lastObject: T?
        get() = if (dataList.size > 0) dataList[dataList.size - 1] else null
    var page: Int = 1
        private set
    var error: Throwable? = null
    var maxCount: Int = 0

    fun replace(index: Int, entity: T) {
        synchronized(dataList) {
            dataList.removeAt(index)
            dataList.add(index, entity)
        }
    }

    fun addResult(result: List<T>) {
        dataList.addAll(result)
        page++
    }

    fun getPage(refresh: Boolean): Int {
        if (refresh) page = 1
        return page
    }

    fun clean() {
        dataList.clear()
        error = null
        page = 1
    }

    fun size(): Int {
        return dataList.size
    }

    fun remove(index: Int) {
        dataList.removeAt(index)
    }
}

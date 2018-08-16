package com.movies.popular.popularmovies.data.common.cashe

import java.util.*

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 10/17/17
 * Time: 4:39 PM
 */

class Page<T>(var hasNext: Boolean = false,
              var maxCount: Int = 0) {

    private var dataList: MutableList<T> = ArrayList<T>()

    var page: Int = 0

    val lastObject: T?
        get() = if (dataList.size > 0) dataList[dataList.size - 1] else null

    fun getDataList(): List<T> {
        return dataList
    }

    fun replace(index: Int, entity: T) {
        dataList.removeAt(index)
        dataList.add(index, entity)
    }

    fun addResult(result: List<T>) {
        dataList.addAll(result)
        page++
    }

    fun clean() {
        dataList.clear()
        page = 0
    }

    fun size(): Int {
        return dataList.size
    }
}

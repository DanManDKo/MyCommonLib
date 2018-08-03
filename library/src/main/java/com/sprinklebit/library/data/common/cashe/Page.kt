package com.sprinklebit.library.data.common.cashe

import java.util.*

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 10/17/17
 * Time: 4:39 PM
 */

class Page<T>(private val keyCallback: (T) -> Any,
              var hasNext: Boolean = false,
              var maxCount: Int = 0) {

    private var keyList: MutableList<Any> = ArrayList()
    private var dataList: MutableList<T> = ArrayList()

    private var page: Int = 0

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
        for (t in result) {
            dataList.add(t)
            keyList.add(keyCallback.invoke(t))
        }
        page++
    }

    fun getPage(refresh: Boolean): Int {
        if (refresh) page = 0
        return page
    }

    fun leftOnly(count: Int) {
        if (keyList.size > count) {
            keyList = keyList.subList(0, count)
            dataList = dataList.subList(0, count)
        }
    }

    fun clean() {
        keyList.clear()
        dataList.clear()
        page = 0
    }

    fun update(t: T) {
        val indexOf = keyList.indexOf(keyCallback.invoke(t))
        dataList.removeAt(indexOf)
        dataList.add(indexOf, t)
    }

    fun find(key: Any): T? {
        val indexOf = keyList.indexOf(key)
        return if (indexOf == -1) {
            null
        } else dataList[indexOf]
    }

    fun size(): Int {
        return dataList.size
    }
}

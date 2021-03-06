package com.sprinklebit.library.data.common.mappers

import java.util.*
import java.util.Collections.emptyList

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/4/17
 * Time: 7:50 PM
 */
object Mappers {

    @JvmStatic
    fun <F, T> mapCollection(list: List<F>?, mapper: Mapper<F, T>): List<T> {
        return if (list == null) {
            emptyList()
        } else {
            val size = list.size
            val result = ArrayList<T>(size)

            for (i in 0 until size) {
                val map = mapper.map(list[i])
                if (map != null) {
                    result.add(map)
                }
            }

            result
        }
    }
}

package com.sprinklebit.library.data.common.mappers

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/4/17
 * Time: 7:50 PM
 */

interface Mapper<F, T> {
    fun map(value: F): T
}


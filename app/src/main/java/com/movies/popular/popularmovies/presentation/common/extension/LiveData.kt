package com.movies.popular.popularmovies.presentation.common.extension

import android.arch.lifecycle.MutableLiveData

/**
 * User: Sasha Shcherbinin
 * Date : 7/6/18
 */

/**
 * Set value only if previous was null or not set
 */
fun <T> MutableLiveData<T>.setValueIfEmpty(value: T?) {
    if (this.value == null) {
        setValueIgnoreNull(value)
    }
}

/**
 * Set value only if new is not null
 */
fun <T> MutableLiveData<T>.setValueIgnoreNull(value: T?) {
    value?.let { setValue(value) }
}
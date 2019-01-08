package com.movies.popular.popularmovies.presentation.common

import androidx.annotation.IntDef

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 1/24/18
 * Time: 11:05 PM
 */

class ContentState private constructor(@Visibility private val visibility: Int) {

    val isContent: Boolean get() = visibility == CONTENT

    val isLoading: Boolean get() = visibility == LOADING

    val isEmpty: Boolean get() = visibility == EMPTY

    val isError: Boolean get() = visibility == ERROR

    @IntDef(CONTENT, LOADING, EMPTY, ERROR)
    internal annotation class Visibility

    companion object {
        private const val CONTENT = 1
        private const val LOADING = 2
        private const val EMPTY = 3
        private const val ERROR = 4

        val STATE_CONTENT = ContentState(CONTENT)
        val STATE_LOADING = ContentState(LOADING)
        val STATE_EMPTY = ContentState(EMPTY)
        val STATE_ERROR = ContentState(ERROR)
    }
}

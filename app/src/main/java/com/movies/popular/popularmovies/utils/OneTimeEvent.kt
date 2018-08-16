package com.movies.popular.popularmovies.utils

/**
 * User: Sasha Shcherbinin
 * Date : 5/22/18
 */
class OneTimeEvent constructor(private val event: () -> Unit) {

    private var firstTime: Boolean = true

    fun invoke() {
        if (firstTime) {
            event.invoke()
        }
        firstTime = false
    }

    fun reset() {
        firstTime = true
    }
}
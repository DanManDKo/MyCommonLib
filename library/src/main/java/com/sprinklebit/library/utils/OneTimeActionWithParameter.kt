package com.sprinklebit.library.utils

/**
 * User: Sasha Shcherbinin
 * Date : 5/22/18
 */
class OneTimeActionWithParameter<T> constructor(private val event: (T) -> Unit) {

    private var t: T? = null

    fun invoke(t: T) {
        if (this.t != t) {
            this.t = t
            event.invoke(t)
        }
    }

    fun reset() {
        t = null
    }
}
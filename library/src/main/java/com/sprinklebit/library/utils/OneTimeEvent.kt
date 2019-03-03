package com.sprinklebit.library.utils

/**
 * User: Sasha Shcherbinin
 * Date : 5/22/18
 */
@Deprecated("Check on saveInstance == null and call method with parameters.")
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
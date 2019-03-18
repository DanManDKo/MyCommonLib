package com.sprinklebit.library.utils

/**
 * User: Sasha Shcherbinin
 * Date : 5/22/18
 */
@Deprecated("use OneTimeActionWithParameter instead")
class OneTimeAction constructor(private val event: () -> Unit) {

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
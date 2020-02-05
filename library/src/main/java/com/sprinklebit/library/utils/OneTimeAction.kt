package com.sprinklebit.library.utils

internal class OneTimeAction<T> constructor(private val event: (T) -> Unit) {

    private var t: T? = null

    fun invoke(t: T) {
        if (t != null && this.t != null && t is Array<*>) {
            if (!(t as Array<*>).contentEquals(this.t as Array<*>)) {
                this.t = t
                event.invoke(t)
            }
        } else {
            if (this.t != t) {
                this.t = t
                event.invoke(t)
            }
        }
    }

    fun reset() {
        t = null
    }
}
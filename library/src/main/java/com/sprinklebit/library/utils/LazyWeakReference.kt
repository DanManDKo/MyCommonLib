package com.sprinklebit.library.utils

import java.lang.ref.WeakReference

/**
 * User: Sasha Shcherbinin
 * Date : 7/14/18
 */
class LazyWeakReference<T> constructor(private val creation: () -> T) {

    private var weakReference: WeakReference<T>? = null

    fun get(): T {
        if (weakReference == null) {
            val referent = creation.invoke()
            weakReference = WeakReference(referent)
            return referent
        } else {
            val get = weakReference!!.get()
            if (get == null) {
                val referent = creation.invoke()
                weakReference = WeakReference(referent)
                return referent
            } else return get
        }
    }
}

package com.sprinklebit.library.helper

import io.reactivex.Observable

object NetworkStateRxHelper {

    fun checkConnection(): Observable<Boolean> {
        return Observable.create<Boolean> {
            val listener = object : NetworkStateHelper.ChangeListener {
                override fun onChange(isConnected: Boolean) {
                    if (!it.isDisposed) {
                        it.onNext(isConnected)
                    }
                }
            }
            NetworkStateHelper.subscribe(listener)
            it.setCancellable { NetworkStateHelper.unsubscribe(listener) }
        }
    }
}
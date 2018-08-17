package com.sprinklebit.library.utils.rx

import java.util.HashMap

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/20/17
 */
object RxDisposable {

    private val sSubscriptions = HashMap<Any, SpecificCompositeDisposable>()

    @JvmStatic
    fun manage(tag: Any, subscription: Disposable) {
        var subscriptions: SpecificCompositeDisposable? = sSubscriptions[tag]
        if (subscriptions == null) {
            subscriptions = SpecificCompositeDisposable()
            sSubscriptions[tag] = subscriptions
        }

        subscriptions.add(subscription)
    }

    @JvmStatic
    fun manage(tag: Any, subscriptionTab: Any, subscription: Disposable?) {
        var subscriptions: SpecificCompositeDisposable? = sSubscriptions[tag]
        if (subscriptions == null) {
            subscriptions = SpecificCompositeDisposable()
            sSubscriptions[tag] = subscriptions
        }

        subscriptions.add(subscriptionTab, subscription)
    }

    @JvmStatic
    fun unsubscribe(tag: Any) {
        val subscriptions = sSubscriptions[tag]
        if (subscriptions != null) {
            subscriptions.dispose()
            sSubscriptions.remove(tag)
        }
    }

    @JvmStatic
    fun unsubscribe(tag: Any, subscriptionTab: Any) {
        val subscriptions = sSubscriptions.get(tag)
        if (subscriptions != null) {
            subscriptions.dispose(subscriptionTab)
        }
    }

    private class SpecificCompositeDisposable {

        internal val mCompositeDisposable = CompositeDisposable()
        internal val mDisposableHashMap = HashMap<Any, Disposable>()

        internal fun dispose() {
            mCompositeDisposable.dispose()
        }

        internal fun dispose(subscriptionTab: Any) {
            val oldSubscription = mDisposableHashMap[subscriptionTab]
            if (oldSubscription != null) {
                mCompositeDisposable.remove(oldSubscription)
            }
        }

        internal fun add(disposable: Disposable): Boolean {
            return mCompositeDisposable.add(disposable)
        }

        internal fun add(subscriptionTab: Any, disposable: Disposable?): Boolean {
            if (disposable == null) {
                return false
            }
            val oldDisposable = mDisposableHashMap[subscriptionTab]
            if (oldDisposable != null) {
                mCompositeDisposable.remove(oldDisposable)
            }
            mDisposableHashMap[subscriptionTab] = disposable
            return mCompositeDisposable.add(disposable)
        }
    }

}

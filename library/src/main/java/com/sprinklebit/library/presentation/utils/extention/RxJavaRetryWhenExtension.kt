package com.sprinklebit.library.presentation.utils.extention

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * @param delay the initial delay before emitting, default value 5
 * @param timeUnit time units to use for delay, default TimeUnit.SECONDS
 */
fun <T> Observable<T>.defaultRetryWhen(
        delay: Long = 5,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        vararg acceptableErrors: KClass<out Throwable>
): Observable<T> {
    return this.retryWhen { observable ->
        observable.flatMap {
            if (acceptableErrors.contains(it::class)) {
                Observable.error<Int>(it)
            } else {
                Observable.timer(delay, timeUnit,
                        AndroidSchedulers.mainThread())
            }
        }
    }
}

/**
 * @param delay the initial delay before emitting, default value 5
 * @param timeUnit time units to use for delay, default TimeUnit.SECONDS
 */
fun <T> Single<T>.defaultRetryWhen(
        delay: Long = 5,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        vararg acceptableErrors: KClass<out Throwable>
): Single<T> {
    return this.retryWhen { single ->
        single.flatMap {
            if (acceptableErrors.contains(it::class)) {
                Flowable.error<Int>(it)
            } else {
                Flowable.timer(delay, timeUnit,
                        AndroidSchedulers.mainThread())
            }
        }
    }
}

/**
 * @param delay the initial delay before emitting, default value 5
 * @param timeUnit time units to use for delay, default TimeUnit.SECONDS
 */
fun Completable.defaultRetryWhen(
        delay: Long = 5,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        vararg acceptableErrors: KClass<out Throwable>
): Completable {
    return this.retryWhen { completable ->
        completable.flatMap {
            if (acceptableErrors.contains(it::class)) {
                Flowable.error<Int>(it)
            } else {
                Flowable.timer(delay, timeUnit,
                        AndroidSchedulers.mainThread())
            }
        }
    }
}
package com.sprinklebit.library.presentation.common.livedata

import android.os.CountDownTimer
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * PersonalInfo: Sasha Shcherbinin
 * Date : 4/13/18
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the innerObserver is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one innerObserver is going to be notified of changes.
 */
class DebounceSingleLiveEvent<T> : MutableLiveData<T>() {

    private val newValue: T? = null
    private val pending = AtomicBoolean(false)

    private lateinit var timer: CountDownTimer

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registeredObserver but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    fun debounce(millisInFuture: Long, liveData: LiveData<*>) {
        liveData.observeForever {
            timer.cancel()
        }
        timer = object : CountDownTimer(millisInFuture, Long.MAX_VALUE) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                setSuperValue(newValue)
            }

        }
    }

    @MainThread
    private fun setSuperValue(t: T?) {
        super.setValue(t)
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        timer.cancel()
        timer.start()
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}
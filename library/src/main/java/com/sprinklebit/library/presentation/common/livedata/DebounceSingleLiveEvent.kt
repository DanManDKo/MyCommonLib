package com.sprinklebit.library.presentation.common.livedata

import android.os.CountDownTimer
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class DebounceSingleLiveEvent<T>constructor(millisInFuture: Long, liveData: LiveData<*>)
    : MutableLiveData<T>() {

    private val newValue: T? = null
    private val pending = AtomicBoolean(false)

    private lateinit var timer: CountDownTimer

    init {
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
package com.sprinklebit.library.presentation.common.livedata

import android.os.CountDownTimer
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import com.sprinklebit.library.TestConfig

open class DebounceMutableLiveData<T>(millis: Long, val uniquely: Boolean = false) : MutableLiveData<T>() {

    private var newValue: T? = null

    protected var timer = object : CountDownTimer(millis, Long.MAX_VALUE) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            setSuperValue(newValue)
        }
    }

    @MainThread
    private fun setSuperValue(t: T?) {
        super.setValue(t)
    }

    @MainThread
    override fun setValue(t: T?) {
        if (uniquely && newValue == t) return
        newValue = t
        if (TestConfig.isTestEnvironment) {
            setSuperValue(newValue)
        } else {
            timer.cancel()
            timer.start()
        }
    }

}
package com.sprinklebit.library.presentation.common.livedata

import android.os.CountDownTimer
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData

class DebounceMutableLiveData<T>(millis: Long) : MutableLiveData<T>() {

    private var newValue: T? = null

    private var timer = object : CountDownTimer(millis, Long.MAX_VALUE) {
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
        newValue = t
        timer.cancel()
        timer.start()
    }

}
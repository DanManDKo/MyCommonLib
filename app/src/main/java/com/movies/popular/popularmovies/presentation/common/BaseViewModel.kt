package com.movies.popular.popularmovies.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.popular.popularmovies.utils.RxDisposable

/**
 * PersonalInfo: Sasha Shcherbinin
 * Date : 4/9/18
 */
open class BaseViewModel : ViewModel() {

    var errorMessage = SingleLiveEvent<String>()
    var uploadingState = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        RxDisposable.unsubscribe(this)
    }
}

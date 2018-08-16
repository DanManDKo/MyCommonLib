package com.movies.popular.popularmovies.presentation.common.error

import android.content.Context
import com.movies.popular.popularmovies.exeption.APIException
import com.movies.popular.popularmovies.exeption.AuthException
import timber.log.Timber
import javax.inject.Inject

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/26/17
 */
class DefaultErrorHandler
@Inject
constructor(var context: Context) : ErrorHandler {

    override fun handleError(throwable: Throwable) {
        handleError(throwable, null)
    }

    override fun handleError(throwable: Throwable, errorView: ((message: String) -> Unit)?) {
        Timber.e(throwable)

        if (errorView != null) {
            var message: String? = null
            if (throwable is AuthException) {

            } else if (throwable is APIException) {
                message = throwable.message
            } else {
                // todo ass string resource
                message = "Server Error"
            }
            if (message != null) errorView.invoke(message)
        }
    }

}

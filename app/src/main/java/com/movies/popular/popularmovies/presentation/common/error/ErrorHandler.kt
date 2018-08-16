package com.movies.popular.popularmovies.presentation.common.error

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/26/17
 */
interface ErrorHandler {

    fun handleError(throwable: Throwable)

    fun handleError(throwable: Throwable, errorView: ((message: String) -> Unit)?)

}

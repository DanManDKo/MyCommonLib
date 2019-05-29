package com.sprinklebit.library.presentation.common.error

interface ErrorHandler {

    fun handleError(throwable: Throwable)

    fun handleError(throwable: Throwable, errorView: (message: String) -> Unit)

    fun handleJavaError(throwable: Throwable, errorView: ErrorView)

    interface ErrorView {
        fun showError(message: String)
    }
}

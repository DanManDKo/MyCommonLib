package com.sprinklebit.library.presentation.common.error

interface ErrorHandler {

    fun handleError(throwable: Throwable)

    fun handleError(throwable: Throwable, errorView: ErrorView)

    fun handleError(throwable: Throwable, errorView: (message: String) -> Unit)

    interface ErrorView {
        fun showError(message: String)
    }
}

package com.movies.popular.popularmovies.di.module

import com.movies.popular.popularmovies.presentation.common.error.DefaultErrorHandler
import com.movies.popular.popularmovies.presentation.common.error.ErrorHandler
import dagger.Module
import dagger.Provides

/**
 * User: Sasha Shcherbinin
 * Date : 4/22/18
 */
@Module
class PresentationModule {

    @Provides
    internal fun provideDefaultError(errorHandler: DefaultErrorHandler)
            : ErrorHandler = errorHandler
}

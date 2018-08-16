package com.movies.popular.popularmovies.di.module

import com.movies.popular.popularmovies.data.module.movie.MovieRepositoryImpl
import com.movies.popular.popularmovies.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides

/**
 * User: Sasha Shcherbinin
 * Date : 4/22/18
 */
@Module
class RepositoryModule {

    @Provides
    internal fun provideMovieRepositoryImpl(repository: MovieRepositoryImpl)
            : MovieRepository = repository

}

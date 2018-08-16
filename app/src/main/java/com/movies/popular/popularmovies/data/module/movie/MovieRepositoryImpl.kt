package com.movies.popular.popularmovies.data.module.movie

import android.arch.paging.DataSource
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.movies.popular.popularmovies.domain.model.Movie
import com.movies.popular.popularmovies.domain.repository.MovieRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
@Singleton
class MovieRepositoryImpl
@Inject
constructor(private val movieDataSource: MovieDataSource)
    : MovieRepository {

    override fun getMovieList(): Observable<PagedList<Movie>> {
        return movieDataSource.getMovies()
    }

    override fun observeLoading(): Observable<Boolean> {
        return movieDataSource.loadingSubject;
    }

    override fun fetch(): Completable {
        return movieDataSource.fetch()
    }
}
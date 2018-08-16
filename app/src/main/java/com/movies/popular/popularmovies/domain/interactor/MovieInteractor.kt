package com.movies.popular.popularmovies.domain.interactor

import android.arch.paging.PagedList
import com.movies.popular.popularmovies.domain.model.Movie
import com.movies.popular.popularmovies.domain.model.PageBundle
import com.movies.popular.popularmovies.domain.repository.MovieRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
class MovieInteractor
@Inject
constructor(private val movieRepository: MovieRepository) {

    fun getMovieList(): Observable<PagedList<Movie>> {
        return movieRepository.getMovieList()
    }

    fun observeLoading(): Observable<Boolean> {
        return movieRepository.observeLoading();
    }

    fun fetch(): Completable {
        return movieRepository.fetch()
    }
}

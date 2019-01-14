package com.movies.popular.popularmovies.domain.interactor

import androidx.paging.PagedList
import com.movies.popular.popularmovies.domain.model.Movie
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

    fun refresh(): Completable {
        return movieRepository.refresh()
    }

    fun updateMovie(id: Int): Completable {
        return movieRepository.updateMovie(id)
    }

    fun remove(id: Int): Completable {
        return movieRepository.remove(id)
    }
}

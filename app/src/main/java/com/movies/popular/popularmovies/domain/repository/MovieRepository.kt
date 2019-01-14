package com.movies.popular.popularmovies.domain.repository

import androidx.paging.PagedList
import com.movies.popular.popularmovies.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
interface MovieRepository {

    fun getMovieList(): Observable<PagedList<Movie>>

    fun refresh(): Completable

    fun observeLoading(): Observable<Boolean>

    fun updateMovie(id: Int): Completable

    fun remove(id: Int): Completable
}
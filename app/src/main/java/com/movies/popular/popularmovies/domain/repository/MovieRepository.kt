package com.movies.popular.popularmovies.domain.repository

import android.arch.paging.PagedList
import com.movies.popular.popularmovies.domain.model.Movie
import com.movies.popular.popularmovies.domain.model.PageBundle
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
interface MovieRepository {
    fun getMovieList(): Observable<PagedList<Movie>>
    fun fetch(): Completable
    fun observeLoading(): Observable<Boolean>
}
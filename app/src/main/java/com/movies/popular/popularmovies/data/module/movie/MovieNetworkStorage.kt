package com.movies.popular.popularmovies.data.module.movie

import com.movies.popular.popularmovies.data.common.mappers.Mappers
import com.movies.popular.popularmovies.data.network.mapper.MovieResponseMapper
import com.movies.popular.popularmovies.data.network.service.DiscoverService
import com.movies.popular.popularmovies.domain.model.Movie
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 5/9/18
 */
class MovieNetworkStorage
@Inject
constructor(private val discoverService: DiscoverService,
            private val movieResponseMapper: MovieResponseMapper) {

    fun getMovies(page: Int): Single<List<Movie>> {
        return if (page > 2) Single.just(ArrayList())
        else discoverService.getMovies(page = page)
                .subscribeOn(Schedulers.io())
                .map { Mappers.mapCollection(it.results, movieResponseMapper) }
                .map {
                    val arrayList = ArrayList<Movie>()
                    if (page == 1) {
                        val random = Random()
                        for (i in 1..5) {
                            arrayList.add(Movie(i, "2222", "dfasdf", null))
                        }
                    } else if (page == 2) {
                        for (i in 30..40) {
                            arrayList.add(Movie(i, "2222", "dfasdf", null))
                        }
                    }
                    arrayList
                }
    }
}
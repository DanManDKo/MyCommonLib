package com.movies.popular.popularmovies.data.module.movie

import com.movies.popular.popularmovies.data.common.mappers.Mappers
import com.movies.popular.popularmovies.data.network.mapper.MovieResponseMapper
import com.movies.popular.popularmovies.data.network.service.DiscoverService
import com.movies.popular.popularmovies.domain.model.Movie
import io.reactivex.Single
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
        return discoverService.getMovies(page = page)
                .map { Mappers.mapCollection(it.results, movieResponseMapper) }
    }
}
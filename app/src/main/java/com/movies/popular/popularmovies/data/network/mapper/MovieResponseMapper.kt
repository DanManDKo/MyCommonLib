package com.movies.popular.popularmovies.data.network.mapper

import com.movies.popular.popularmovies.data.network.model.MovieDto
import com.movies.popular.popularmovies.domain.model.Movie
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 7/17/18
 */
class MovieResponseMapper
@Inject
constructor() {

    fun map(value: MovieDto): Movie {
        return Movie(value.id, value.overview, value.title, value.posterPath)
    }

}

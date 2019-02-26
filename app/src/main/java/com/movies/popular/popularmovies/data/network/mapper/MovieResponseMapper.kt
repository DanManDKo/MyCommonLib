package com.movies.popular.popularmovies.data.network.mapper

import com.movies.popular.popularmovies.data.network.model.MovieDto
import com.movies.popular.popularmovies.domain.model.Movie
import com.sprinklebit.library.data.common.mappers.Mapper
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 7/17/18
 */
class MovieResponseMapper
@Inject
constructor()
    : Mapper<MovieDto, Movie> {

    override fun map(value: MovieDto): Movie {
        return Movie(value.id ?: 0, value.overview, value.title, value.posterPath)
    }

}

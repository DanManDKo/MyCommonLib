package com.movies.popular.popularmovies.data.network.mapper

import com.movies.popular.popularmovies.data.common.mappers.Mapper
import com.movies.popular.popularmovies.data.network.model.MovieDto
import com.movies.popular.popularmovies.domain.model.Movie
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 7/17/18
 */
class MovieResponseMapper
@Inject
constructor()
    : Mapper<MovieDto, Movie> {

    override fun map(var1: MovieDto?): Movie {
        return Movie(var1?.id ?: 0, var1?.overview, var1?.title, var1?.posterPath)
    }

}

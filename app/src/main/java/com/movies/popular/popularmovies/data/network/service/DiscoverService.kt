package com.movies.popular.popularmovies.data.network.service

import com.movies.popular.popularmovies.data.network.model.DataDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * User: Sasha Shcherbinin
 * Date : 5/14/18
 */
interface DiscoverService {

    @GET("3/discover/movie_")
    fun getMovies(@Query("sort_by") sortBy: String = "popularity.des",
                  @Query("page") page: Int,
                  @Query("with_genres") genres: String = "14")
            : Single<DataDto>


}
package com.movies.popular.popularmovies.domain.model

import com.google.gson.annotations.SerializedName

data class Movie(
        val id: Int = 0,
        val overview: String?,
        val title: String?,
        val posterPath: String?
)
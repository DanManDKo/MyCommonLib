package com.movies.popular.popularmovies.domain.model

/**
 * User: Sasha Shcherbinin
 * Date : 5/21/18
 */
data class PageBundle<E>(val data: List<E>,
                         val hasNext: Boolean,
                         val maxCount: Int = 0)

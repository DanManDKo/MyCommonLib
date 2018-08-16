package com.movies.popular.popularmovies.exeption

/**
 * PersonalInfo: Sasha Shcherbinin
 * Date : 4/6/18
 */
open class APIException(val code: Int, message: String?) : Exception(message) {

    override fun toString(): String {
        return message ?: ""
    }
}

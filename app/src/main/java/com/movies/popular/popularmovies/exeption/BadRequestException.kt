package com.movies.popular.popularmovies.exeption

import com.movies.popular.popularmovies.exeption.APIException

/**
 * PersonalInfo: Sasha Shcherbinin
 * Date : 4/6/18
 */
class BadRequestException(code: Int, message: String?) : APIException(code, message) {

    override fun toString(): String {
        return message ?: ""
    }
}

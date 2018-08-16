package com.movies.popular.popularmovies.data.network.interseptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 10/5/17
 */
class HeaderInterceptor
@Inject
internal constructor()
    : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val newUrl = chain.request().url().newBuilder()
                .addQueryParameter("api_key",
                        "fbe4e6280f6a460beaad8ebe2bc130ac")
                .build()
        val newRequest = chain.request().newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }

}

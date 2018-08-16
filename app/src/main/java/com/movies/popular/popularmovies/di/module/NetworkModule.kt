package com.movies.popular.popularmovies.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.movies.popular.popularmovies.BuildConfig
import com.movies.popular.popularmovies.data.common.RxErrorCallAdapterFactory
import com.movies.popular.popularmovies.data.network.interseptor.HeaderInterceptor
import com.sprinkle.brokerage.di.Qualifier.DefaultQualifier
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 4/18/17
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @DefaultQualifier
    @Singleton
    internal fun provideRetrofit(@DefaultQualifier
                                 okHttp: OkHttpClient,
                                 factory: RxErrorCallAdapterFactory,
                                 gson: Gson)
            : Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.SCHEME + BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(factory)

        builder.client(okHttp)

        return builder.build()
    }

    @Provides
    @DefaultQualifier
    @Singleton
    internal fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(headerInterceptor)
        clientBuilder.connectTimeout(15, TimeUnit.SECONDS)
        clientBuilder.readTimeout(15, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(15, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            clientBuilder.connectTimeout(10, TimeUnit.SECONDS)
            clientBuilder.readTimeout(10, TimeUnit.SECONDS)
            clientBuilder.writeTimeout(10, TimeUnit.SECONDS)
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)
        }

        return clientBuilder.build()
    }
}


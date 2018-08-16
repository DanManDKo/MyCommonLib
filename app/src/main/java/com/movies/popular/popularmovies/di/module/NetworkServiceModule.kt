package com.movies.popular.popularmovies.di.module

import com.movies.popular.popularmovies.data.network.service.DiscoverService
import com.sprinkle.brokerage.di.Qualifier.DefaultQualifier
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 4/18/17
 */
@Module
class NetworkServiceModule {

    @Provides
    @Singleton
    internal fun provideDiscoverService(@DefaultQualifier
                                        retrofit: Retrofit)
            : DiscoverService {
        return retrofit.create(DiscoverService::class.java)
    }

}

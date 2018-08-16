package com.movies.popular.popularmovies.di.module

import android.content.Context

import dagger.Module
import dagger.Provides

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/20/17
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    internal fun provideContext(): Context = context

}

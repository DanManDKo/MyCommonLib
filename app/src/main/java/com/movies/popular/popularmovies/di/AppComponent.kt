package com.movies.popular.popularmovies.di

import com.movies.popular.popularmovies.App
import com.movies.popular.popularmovies.di.module.ActivityModule
import com.movies.popular.popularmovies.di.module.AppModule
import com.movies.popular.popularmovies.di.module.NetworkModule
import com.movies.popular.popularmovies.di.module.NetworkServiceModule
import com.movies.popular.popularmovies.di.module.PresentationModule
import com.movies.popular.popularmovies.di.module.RepositoryModule
import com.movies.popular.popularmovies.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/20/17
 */
@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    NetworkServiceModule::class,
    ActivityModule::class,
    ViewModelModule::class,
    PresentationModule::class,
    RepositoryModule::class
])
interface AppComponent {
    fun inject(app: App)
}

package com.movies.popular.popularmovies.presentation.module.main.popular

import android.support.v4.app.Fragment
import com.movies.popular.popularmovies.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created with Android Studio.
 * User: Danil Konovalenko
 * Date: 5/3/18
 * Time: 2:45 PM
 */
@FragmentScope
@Subcomponent(modules = [
    MoviesFragmentComponent.FragmentModule::class
])
interface MoviesFragmentComponent : AndroidInjector<MoviesFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MoviesFragment>()

    @Module
    class FragmentModule {
        @Provides
        internal fun provideFragment(fragment: MoviesFragment): Fragment = fragment
    }
}
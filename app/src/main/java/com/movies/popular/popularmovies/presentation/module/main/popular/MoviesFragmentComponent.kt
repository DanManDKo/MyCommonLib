package com.movies.popular.popularmovies.presentation.module.main.popular

import com.movies.popular.popularmovies.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector

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
        internal fun provideFragment(fragment: MoviesFragment): androidx.fragment.app.Fragment = fragment
    }
}
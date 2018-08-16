package com.movies.popular.popularmovies.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.movies.popular.popularmovies.di.ViewModelKey
import com.movies.popular.popularmovies.presentation.common.ViewModelFactory
import com.movies.popular.popularmovies.presentation.module.main.popular.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * PersonalInfo: Sasha Shcherbinin
 * Date : 3/25/18
 */
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
package com.movies.popular.popularmovies.presentation.module.main

import com.movies.popular.popularmovies.R
import com.movies.popular.popularmovies.di.scope.ActivityScope
import com.movies.popular.popularmovies.presentation.module.main.popular.MoviesFragment
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
@ActivityScope
class MainRouter
@Inject
constructor(val mainActivity: MainActivity) {

    fun showMoviesFragment() {
        mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesFragment.newInstance())
                .commit()
    }
}
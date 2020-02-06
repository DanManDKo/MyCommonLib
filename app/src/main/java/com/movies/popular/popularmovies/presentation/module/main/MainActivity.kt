package com.movies.popular.popularmovies.presentation.module.main

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import com.movies.popular.popularmovies.R
import com.movies.popular.popularmovies.databinding.ActivityMainBinding
import com.movies.popular.popularmovies.presentation.common.BaseActivity
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
class MainActivity : BaseActivity() {

    @Inject
    lateinit var mainRouter: MainRouter

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) mainRouter.showMoviesFragment()
        setSupportActionBar(binding.toolbar)
    }

}

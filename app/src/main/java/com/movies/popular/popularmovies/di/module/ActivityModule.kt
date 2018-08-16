package com.movies.popular.popularmovies.di.module

import android.app.Activity
import com.movies.popular.popularmovies.presentation.module.main.MainActivity
import com.movies.popular.popularmovies.presentation.module.main.MainActivityComponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/20/17
 */
@Module(subcomponents = [
    MainActivityComponent::class
])
internal abstract class ActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivityInjectorFactory(
            builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>

}


package com.movies.popular.popularmovies.presentation.module.main;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.movies.popular.popularmovies.di.scope.ActivityScope;
import com.movies.popular.popularmovies.presentation.module.main.popular.MoviesFragment;
import com.movies.popular.popularmovies.presentation.module.main.popular.MoviesFragmentComponent;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

@ActivityScope
@Subcomponent(modules = {
        MainActivityComponent.EducationPageActivityModule.class,
        MainActivityComponent.FragmentBindingsModule.class,
})
public interface MainActivityComponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    }

    @Module
    class EducationPageActivityModule {
        @Provides
        Activity provideActivity(MainActivity activity) {
            return activity;
        }

    }

    @Module(subcomponents = {
            MoviesFragmentComponent.class
    })
    abstract class FragmentBindingsModule {

        @Binds
        @IntoMap
        @FragmentKey(MoviesFragment.class)
        public abstract AndroidInjector.Factory<? extends Fragment> movieFragmentComponentBuilder(
                MoviesFragmentComponent.Builder builder);

    }

}

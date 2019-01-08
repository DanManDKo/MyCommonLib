package com.movies.popular.popularmovies

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.evernote.android.state.StateSaver
import com.movies.popular.popularmovies.di.AppComponent
import com.movies.popular.popularmovies.di.DaggerAppComponent
import com.movies.popular.popularmovies.di.module.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject


/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/20/17
 */
class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)

        initAndroidState()
        initTimber()
    }

    private fun initAndroidState() {
        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

}

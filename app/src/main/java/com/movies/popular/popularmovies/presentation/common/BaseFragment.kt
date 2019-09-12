package com.movies.popular.popularmovies.presentation.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hannesdorfmann.fragmentargs.FragmentArgs

import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 8/20/17
 */
open class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        try {
            if (hasInjection()) {
                AndroidSupportInjection.inject(this)
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FragmentArgs.inject(this)
        super.onCreate(savedInstanceState)
    }

    open fun hasInjection(): Boolean {
        return true
    }

    fun setTitle(title: String?) {
        val activity = activity as AppCompatActivity
        val supportActionBar = activity.supportActionBar
        if (supportActionBar != null) {
            supportActionBar.title = title
        } else {
            Timber.e("You do not set support action bar")
        }
    }

    fun setSubtitle(subtitle: String?) {
        val activity = activity as AppCompatActivity
        val supportActionBar = activity.supportActionBar
        if (supportActionBar != null) {
            supportActionBar.subtitle = subtitle
        } else {
            Timber.e("You do not set support action bar")
        }
    }
}

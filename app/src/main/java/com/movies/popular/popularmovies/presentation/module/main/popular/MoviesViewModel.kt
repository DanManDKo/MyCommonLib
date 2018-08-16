package com.movies.popular.popularmovies.presentation.module.main.popular

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.movies.popular.popularmovies.domain.interactor.MovieInteractor
import com.movies.popular.popularmovies.domain.model.Movie
import com.movies.popular.popularmovies.presentation.common.BaseViewModel
import com.movies.popular.popularmovies.presentation.common.ContentState
import com.movies.popular.popularmovies.presentation.common.SingleLiveEvent
import com.movies.popular.popularmovies.presentation.common.error.ErrorHandler
import com.movies.popular.popularmovies.utils.RxDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created with Android Studio.
 * User: Danil Konovalenko
 * Date: 5/3/18
 * Time: 2:39 PM
 */
class MoviesViewModel
@Inject
constructor(private val movieInteractor: MovieInteractor,
            private val errorHandler: ErrorHandler)
    : BaseViewModel() {

    var contentState = MutableLiveData<ContentState>()
    var content = MutableLiveData<PagedList<Movie>>()
    var loading = MutableLiveData<Boolean>()
    var itemClickedEvent = SingleLiveEvent<ContentState>()
    var refreshEvent = MutableLiveData<Boolean>()

    init {
        observeMovies()
        observeLoading()
    }

    private fun observeMovies() {
        RxDisposable.manage(this, "positions",
                movieInteractor.getMovieList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { contentState.setValue(ContentState.STATE_LOADING) }
                        .doOnError { contentState.setValue(ContentState.STATE_ERROR) }
                        .doOnNext {
                            if (it.isEmpty()) contentState.setValue(ContentState.STATE_EMPTY)
                            else contentState.setValue(ContentState.STATE_CONTENT)
                        }
                        .subscribe({
                            content.value = it
                        }, ({
                            errorHandler.handleError(it)
                            { message -> errorMessage.setValue(message) }
                        }))
        )
    }

    private fun observeLoading() {
        RxDisposable.manage(this, "loading",
                movieInteractor.observeLoading()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            loading.value = it
                        }, ({ errorHandler.handleError(it) })))
    }

    fun onItemClicked(movie: Movie) {
        itemClickedEvent.call()
    }

    fun onRefresh() {
        RxDisposable.manage(this, "refresh",
                movieInteractor.fetch()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            refreshEvent.value = false
                        }, ({
                            errorHandler.handleError(it) { message -> errorMessage.setValue(message) }
                        })))
    }
}
package com.movies.popular.popularmovies.presentation.module.main.popular

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.movies.popular.popularmovies.domain.interactor.MovieInteractor
import com.movies.popular.popularmovies.domain.model.Movie
import com.movies.popular.popularmovies.presentation.common.BaseViewModel
import com.movies.popular.popularmovies.presentation.common.ContentState
import com.movies.popular.popularmovies.presentation.common.SingleLiveEvent
import com.movies.popular.popularmovies.presentation.common.error.ErrorHandler
import com.movies.popular.popularmovies.utils.RxDisposable
import com.sprinklebit.library.presentation.utils.extension.observeForever
import io.reactivex.android.schedulers.AndroidSchedulers
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
        RxDisposable.manage(this, "update",
                movieInteractor.updateMovie(movie.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ }, ({ errorHandler.handleError(it) })))
//        itemClickedEvent.call()
    }

    fun onDeleteItem(movie: Movie) {
        RxDisposable.manage(this, "delete",
                movieInteractor.remove(movie.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ }, ({ errorHandler.handleError(it) })))
    }

    fun onRefresh() {
        RxDisposable.manage(this, "refresh",
                movieInteractor.refresh()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            refreshEvent.value = false
                        }, ({
                            errorHandler.handleError(it) { message -> errorMessage.setValue(message) }
                        })))
    }
}
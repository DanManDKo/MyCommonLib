package com.movies.popular.popularmovies.data.module.movie

import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.movies.popular.popularmovies.data.common.MemoryListStorage2
import com.movies.popular.popularmovies.data.common.cashe.CachePolicy
import com.movies.popular.popularmovies.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 8/11/18
 */
class MovieDataSource
@Inject
constructor(private val movieNetworkStorage: MovieNetworkStorage)
    : PageKeyedDataSource<Int, Movie>() {

    private val memoryStorage = MemoryListStorage2.Builder<Unit, Movie>()
            .cachePolicy(CachePolicy.create(5, TimeUnit.MINUTES))
            .capacity(1)
            .build()

    private val refreshSubject = PublishSubject.create<Unit>()
    val loadingSubject = ReplaySubject.create<Boolean>(1)

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        var page = memoryStorage[Unit]
        if (page.size() == 0) {
            val blockingGet = movieNetworkStorage.getMovies(1).blockingGet()
            memoryStorage.add(Unit, blockingGet)
            page = memoryStorage[Unit]
            page.hasNext = true
        }
        loadingSubject.onNext(true)
        callback.onResult(page.getDataList(), null,
                if (page.hasNext) page.page + 1 else null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val newData = movieNetworkStorage.getMovies(params.key).blockingGet()
        memoryStorage.add(Unit, newData)
        val next = if (newData.isNotEmpty()) params.key + 1 else null
        if (next == null) loadingSubject.onNext(false)
        callback.onResult(newData, next)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }

    fun fetch(): Completable {
        return movieNetworkStorage.getMovies(1)
                .doOnSuccess { memoryStorage.put(Unit, it) }
                .doOnSuccess { refreshSubject.onNext(Unit) }
                .ignoreElement()
    }

    fun getMovies(): Observable<PagedList<Movie>> {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build()

        return refreshSubject.mergeWith(Observable.just(Unit))
                .doOnNext { Timber.d(it.toString()) }
                .switchMap {
                    RxPagedListBuilder(object : Factory<Int, Movie>() {
                        override fun create(): DataSource<Int, Movie> {
                            return this@MovieDataSource
                        }
                    }, pagedListConfig)
                            .buildObservable()
                }
    }
}
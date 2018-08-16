package com.sprinklebit.library.data.common

import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.sprinklebit.library.data.common.cashe.CachePolicy
import com.sprinklebit.library.data.common.cashe.CachedEntry
import com.sprinklebit.library.data.common.cashe.ObservableLruCache
import com.sprinklebit.library.data.common.cashe.Page
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import timber.log.Timber

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 2/25/18
 */
class MemoryDataSource<Query, Entity>
private constructor(private val max: Int,
                    private val limit: Int,
                    private val prefetchDistance: Int,
                    private val cachePolicy: CachePolicy,
                    private val fetcher: ((Params<Query, Entity>) -> Single<FetchResult<Entity>>)) {

    private val cache: ObservableLruCache<Query, CachedEntry<Page<Entity>>> = ObservableLruCache(max)

    private var updateSubject = PublishSubject.create<Query>()
    private val loadingSubject = ReplaySubject.create<Pair<Query, Boolean>>(1)
    private val errorSubject = PublishSubject.create<Pair<Query, Throwable>>()

    operator fun get(query: Query): Observable<PagedList<Entity>> {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(prefetchDistance)
                .setPageSize(limit)
                .build()

        val dataSource: PageKeyedDataSource<Page<Entity>, Entity> = object
            : PageKeyedDataSource<Page<Entity>, Entity>() {

            override fun loadInitial(params: LoadInitialParams<Page<Entity>>,
                                     callback: LoadInitialCallback<Page<Entity>, Entity>) {
                Timber.d("loadInitial")
                try {
                    val page = cache[query]
                            .filter { cachePolicy.test(it) }
                            .map<Page<Entity>> { it.entry }
                            .blockingGet(Page(true))
                    if (page.size() == 0) {
                        val newList = fetcher.invoke(
                                Params(
                                        query,
                                        page.size(),
                                        page.page,
                                        limit,
                                        page.lastObject
                                ))
                                .blockingGet()
                        page.addResult(newList.data)
                        page.hasNext = newList.hasNext
                        page.maxCount = newList.maxCount
                        cache.put(query, CachePolicy.createEntry(page))
                    }
                    callback.onResult(page.getDataList(),
                            null,
                            if (page.hasNext) page else null)
                    loadingSubject.onNext(Pair(query, page.hasNext))
                } catch (e: Throwable) {
                    errorSubject.onNext(Pair(query, e));
                }
            }

            override fun loadAfter(params: LoadParams<Page<Entity>>,
                                   callback: LoadCallback<Page<Entity>, Entity>) {
                Timber.d("loadAfter")
                try {
                    val page = cache[query]
                            .filter { cachePolicy.test(it) }
                            .map<Page<Entity>> { it.entry }
                            .blockingGet(Page(true))

                    val newList = fetcher.invoke(
                            Params(
                                    query,
                                    page.size(),
                                    page.page,
                                    limit,
                                    page.lastObject
                            ))
                            .blockingGet()
                    page.addResult(newList.data)
                    page.hasNext = newList.hasNext
                    page.maxCount = newList.maxCount
                    cache.put(query, CachePolicy.createEntry(page))

                    if (!page.hasNext) loadingSubject.onNext(Pair(query, false))
                    callback.onResult(newList.data, if (page.hasNext) page else null)
                } catch (e: Throwable) {
                    errorSubject.onNext(Pair(query, e));
                }
            }

            override fun loadBefore(params: LoadParams<Page<Entity>>,
                                    callback: LoadCallback<Page<Entity>, Entity>) {
                // ignore
            }
        }

        return updateSubject.filter { it == query }
                .mergeWith(errorSubject.filter { it.first == query }
                        .doOnNext { throw it.second }
                        .ignoreElements()
                        .toObservable())
                .mergeWith(Observable.just(query))
                .switchMap {
                    Timber.d("updateSubject")
                    RxPagedListBuilder(object : DataSource.Factory<Page<Entity>, Entity>() {
                        override fun create(): DataSource<Page<Entity>, Entity> {
                            return dataSource
                        }
                    }, pagedListConfig)
                            .buildObservable()
                }
    }

    fun observeLoading(query: Query): Observable<Boolean> {
        return loadingSubject.filter { it.first == query }.map { it.second }
    }

    fun refresh(query: Query): Completable {
        return fetcher.invoke(Params(query, limit = limit))
                .doOnSuccess {
                    Timber.d("refresh")
                    cache.clear()
                    val page = Page<Entity>(it.hasNext, it.maxCount)
                    page.addResult(it.data)
                    cache.put(query, CachePolicy.createEntry(page))
                }
                .doOnSuccess { updateSubject.onNext(query) }
                .ignoreElement()
    }

    fun update(filter: (Entity) -> Boolean,
               onUpdateCallback: (Entity) -> Entity)
            : Completable {
        return cache.get()
                .concatMapCompletable { cacheEntity ->
                    Completable.fromAction {
                        val page = cacheEntity.value.entry
                        var changed = false
                        for (index in 0 until page.getDataList().size) {
                            val entity = page.getDataList()[index]
                            if (filter.invoke(entity)) {
                                val newEntity = onUpdateCallback.invoke(entity)
                                page.replace(index, newEntity)
                                changed = true
                            }
                        }
                        if (changed) {
                            cache.put(cacheEntity.key, CachePolicy.createEntry(page))
                            updateSubject.onNext(cacheEntity.key)
                        }
                    }
                }
    }

    class Builder<Query, Entity>(
            private val fetcher: ((Params<Query, Entity>) -> Single<FetchResult<Entity>>)
    ) {

        private var max = 50
        private var limit = 10
        private var cachePolicy: CachePolicy? = null
        private var prefetchDistance: Int = 5

        fun capacity(max: Int): Builder<Query, Entity> {
            this.max = max
            return this
        }

        fun limit(limit: Int): Builder<Query, Entity> {
            this.limit = limit
            return this
        }

        fun cachePolicy(cachePolicy: CachePolicy): Builder<Query, Entity> {
            this.cachePolicy = cachePolicy
            return this
        }

        fun prefetchDistance(prefetchDistance: Int): Builder<Query, Entity> {
            this.prefetchDistance = prefetchDistance
            return this
        }

        fun build(): MemoryDataSource<Query, Entity> {
            if (cachePolicy == null) {
                cachePolicy = CachePolicy.infinite()
            }
            return MemoryDataSource(
                    max,
                    limit,
                    prefetchDistance,
                    cachePolicy!!,
                    fetcher)
        }
    }

    data class Params<Query, Entity> internal constructor(
            var query: Query,
            var size: Int = 0,
            var page: Int = 1,
            var limit: Int,
            var entity: Entity? = null
    )

    data class FetchResult<E>(
            val data: List<E>,
            val hasNext: Boolean,
            val maxCount: Int = -1
    )


}

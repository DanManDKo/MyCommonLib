package com.sprinklebit.library.data.common

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.sprinklebit.library.data.common.cashe.CachePolicy
import com.sprinklebit.library.data.common.cashe.CachedEntry
import com.sprinklebit.library.data.common.cashe.ObservableLruCache
import com.sprinklebit.library.data.common.cashe.Page
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 2/25/18
 */
@Deprecated("Use memory PagedListStorage")
class MemoryDataSource<Query, Entity>
private constructor(capacity: Int,
                    private val limit: Int,
                    private val initialLoadSizeHint: Int,
                    private val prefetchDistance: Int,
                    private val enablePlaceholders: Boolean,
                    private val cachePolicy: CachePolicy,
                    private val mapBeforeUpdate: ((List<Entity>) -> List<Entity>)? = null,
                    private val fetcher: ((Params<Query, Entity>) -> Single<FetchResult<Entity>>)) {

    private val cache: ObservableLruCache<Query, CachedEntry<Page<Entity>>> = ObservableLruCache(capacity)

    private var updateSubject = PublishSubject.create<Query>()
    private val loadingSubject = ReplaySubject
            .create<Pair<Query, Boolean>>(capacity)
    private val errorSubject = PublishSubject
            .create<Pair<Query, Throwable>>()

    operator fun get(query: Query): Observable<PagedList<Entity>> {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(enablePlaceholders)
                .setPrefetchDistance(prefetchDistance)
                .setInitialLoadSizeHint(initialLoadSizeHint)
                .setPageSize(limit)
                .build()

        val dataSource: PageKeyedDataSource<Page<Entity>, Entity> = object
            : PageKeyedDataSource<Page<Entity>, Entity>() {

            override fun loadInitial(params: LoadInitialParams<Page<Entity>>,
                                     callback: LoadInitialCallback<Page<Entity>, Entity>) {
                try {
                    val page = cache[query]
                            .filter { cachePolicy.test(it) }
                            .map<Page<Entity>> { it.entry }
                            .blockingGet(Page(true))
                    var loadingWasShown = false
                    if (page.hasNext) {
                        loadingOnNextSynchronized(Pair(query, true))
                        loadingWasShown = true
                    }
                    if (page.size() == 0) {
                        val newList = fetcher.invoke(
                                Params(
                                        query,
                                        page.size(),
                                        page.page,
                                        params.requestedLoadSize,
                                        page.getLastObject()
                                ))
                                .blockingGet()
                        page.addResult(newList.data)
                        page.hasNext = newList.hasNext
                        page.maxCount = newList.maxCount
                        mapBeforeUpdate?.invoke(page.dataList)
                        cache.put(query, CachePolicy.createEntry(page))
                    }
                    if (page.maxCount > 0) {
                        callback.onResult(ArrayList(page.dataList), 0, page.maxCount,
                                null,
                                if (page.hasNext) page else null)
                    } else {
                        callback.onResult(ArrayList(page.dataList), null,
                                if (page.hasNext) page else null)
                    }
                    if (!page.hasNext && loadingWasShown) {
                        loadingOnNextSynchronized(Pair(query, false))
                    }
                } catch (e: Throwable) {
                    var trueError = e
                    if (trueError is RuntimeException) {
                        e.cause?.let { trueError = it }
                    }
                    loadingOnNextSynchronized(Pair(query, false))
                    errorSubject.onNext(Pair(query, trueError))
                }
            }

            override fun loadAfter(params: LoadParams<Page<Entity>>,
                                   callback: LoadCallback<Page<Entity>, Entity>) {
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
                                    params.requestedLoadSize,
                                    page.getLastObject()
                            ))
                            .blockingGet()
                    page.addResult(newList.data)
                    page.hasNext = newList.hasNext
                    page.maxCount = newList.maxCount
                    mapBeforeUpdate?.invoke(page.dataList)
                    cache.put(query, CachePolicy.createEntry(page))

                    if (!page.hasNext) {
                        loadingOnNextSynchronized(Pair(query, false))
                    }
                    callback.onResult(newList.data, if (page.hasNext) page else null)
                } catch (e: Throwable) {
                    var trueError = e
                    if (trueError is RuntimeException) {
                        e.cause?.let { trueError = it }
                    }
                    loadingOnNextSynchronized(Pair(query, false))
                    errorSubject.onNext(Pair(query, trueError))
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
                    RxPagedListBuilder(object : DataSource.Factory<Page<Entity>, Entity>() {
                        override fun create(): DataSource<Page<Entity>, Entity> {
                            return dataSource
                        }
                    }, pagedListConfig)
                            .setFetchScheduler(Schedulers.io())
                            .buildObservable()
                }
    }

    fun observeLoading(query: Query): Observable<Boolean> {
        return loadingSubject.filter { it.first == query }.map { it.second }
    }

    fun refresh(query: Query): Completable {
        return fetcher.invoke(Params(query, limit = initialLoadSizeHint))
                .doOnSuccess {
                    cache.clear()
                    val page = Page<Entity>(it.hasNext)
                    page.maxCount = it.maxCount
                    mapBeforeUpdate?.invoke(it.data)
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
                        for (index in 0 until page.dataList.size) {
                            val entity = page.dataList[index]
                            if (filter.invoke(entity)) {
                                val newEntity = onUpdateCallback.invoke(entity)
                                page.replace(index, newEntity)
                                changed = true
                            }
                        }
                        if (changed) {
                            mapBeforeUpdate?.invoke(page.dataList)
                            cache.put(cacheEntity.key, CachePolicy.createEntry(page))
                            updateSubject.onNext(cacheEntity.key)
                        }
                    }
                }
    }

    fun remove(query: Query, filter: (Entity) -> Boolean)
            : Completable {
        return cache[query]
                .flatMapCompletable { cacheEntity ->
                    removeEntity(cacheEntity, query, filter)
                }
    }

    fun remove(filter: (Entity) -> Boolean): Completable {
        return cache.get()
                .concatMapCompletable { cacheEntity ->
                    removeEntity(cacheEntity.value, cacheEntity.key, filter)
                }
    }

    private fun removeEntity(cacheEntity: CachedEntry<Page<Entity>>, query: Query,
                             filter: (Entity) -> Boolean): Completable {
        return Completable.fromAction {
            val page = cacheEntity.entry
            var changed = false
            for (index in page.dataList.size - 1 downTo 0) {
                val entity = page.dataList[index]
                if (filter.invoke(entity)) {
                    page.remove(index)
                    changed = true
                }
            }
            if (changed) {
                mapBeforeUpdate?.invoke(page.dataList)
                cache.put(query, CachePolicy.createEntry(page))
                updateSubject.onNext(query)
            }
        }
    }

    class Builder<Query, Entity>(
            private val fetcher: ((Params<Query, Entity>) -> Single<FetchResult<Entity>>)
    ) {

        private var capacity = 50
        private var initialLoadSizeHint: Int? = null
        private var limit = 10
        private var cachePolicy: CachePolicy? = null
        private var prefetchDistance: Int = 5
        private var enablePlaceholders: Boolean = false
        private var mapBeforeUpdate: ((List<Entity>) -> List<Entity>)? = null

        fun capacity(capacity: Int): Builder<Query, Entity> {
            this.capacity = capacity
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

        fun initialLoadSizeHint(initialLoadSizeHint: Int): Builder<Query, Entity> {
            this.initialLoadSizeHint = initialLoadSizeHint
            return this
        }

        fun enablePlaceholders(enablePlaceholders: Boolean): Builder<Query, Entity> {
            this.enablePlaceholders = enablePlaceholders
            return this
        }

        /**
         * @param mapBeforeUpdate  apply a transform on a list of Entity before any update
         */
        fun mapBeforeUpdate(mapBeforeUpdate: ((List<Entity>) -> List<Entity>)): Builder<Query, Entity> {
            this.mapBeforeUpdate = mapBeforeUpdate
            return this
        }

        fun build(): MemoryDataSource<Query, Entity> {
            if (cachePolicy == null) {
                cachePolicy = CachePolicy.infinite()
            }

            return MemoryDataSource(
                    capacity,
                    limit,
                    initialLoadSizeHint ?: limit,
                    prefetchDistance,
                    enablePlaceholders,
                    cachePolicy!!,
                    mapBeforeUpdate,
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

    @Synchronized
    private fun loadingOnNextSynchronized(pair: Pair<Query, Boolean>) {
        loadingSubject.onNext(pair)
    }

}

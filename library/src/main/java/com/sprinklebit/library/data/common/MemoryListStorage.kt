package com.sprinklebit.library.data.common

import com.sprinklebit.library.data.common.cashe.CachePolicy
import com.sprinklebit.library.data.common.cashe.CachedEntry
import com.sprinklebit.library.data.common.cashe.ObservableLruCache
import com.sprinklebit.library.data.common.cashe.Page
import com.sprinklebit.library.domain.model.PageBundle
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 2/25/18
 */
@Deprecated("Use MemoryDataSource or MemoryListStorageNew objects")
class MemoryListStorage<Query, Entity>
private constructor(max: Int,
                    private val limit: Int,
                    private val cachePolicy: CachePolicy,
                    private val fetcher: ((Params<Query, Entity>) -> Single<FetchResult<Entity>>)) {

    private val cache: ObservableLruCache<Query, CachedEntry<Page<Entity>>> = ObservableLruCache(max)

    private val updateSubject = PublishSubject.create<Query>()
    private val fetchMap = ConcurrentHashMap<Query, Observable<Any>>()

    operator fun get(query: Query): Observable<PageBundle<Entity>> {
        val objectObservable = cache[query]
                .filter { cachePolicy.test(it) }
                .map<Page<Entity>> { it.entry }
                .map<PageBundle<Entity>> { PageBundle(it.getDataList(), it.hasNext, it.maxCount) }
                .toObservable()
        return objectObservable.repeatWhen { updateSubject.filter { q -> query == q } }
                .mergeWith(fetchIfExpired(query))
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

    private fun fetchIfExpired(query: Query): Observable<PageBundle<Entity>> {
        return cache[query]
                .map { cachedEntry -> !cachePolicy.test(cachedEntry) }
                .defaultIfEmpty(true)
                .flatMapCompletable { expired ->
                    if (expired) {
                        Completable.create {
                            val exception = fetchNext(query, true).blockingGet()
                            if (exception != null && !it.isDisposed) {
                                it.onError(exception)
                            }
                            it.onComplete()
                        }

                    } else {
                        Completable.complete()
                    }
                }.toObservable()
    }

    private fun getParams(refresh: Boolean, page: Page<Entity>, query: Query): Params<Query, Entity> {
        return if (refresh)
            Params(query, 0, limit, null)
        else
            Params(query, page.size(), limit, page.lastObject)
    }

    fun fetchNext(query: Query, refresh: Boolean): Completable {
        var observable: Observable<Any>? = fetchMap[query]
        if (observable == null) {
            observable = cache[query]
                    .defaultIfEmpty(CachePolicy.createEntry(Page()))
                    .map<Page<Entity>> { it.entry }
                    .flatMapObservable { page ->
                        fetch(getParams(refresh, page, query))
                                .doOnSuccess { result ->
                                    if (refresh) page.clean()
                                    page.addResult(result.data)
                                    page.hasNext = result.hasNext
                                    page.maxCount = result.maxCount
                                    cache.put(query, CachePolicy.createEntry(page))
                                    updateSubject.onNext(query)
                                }
                                .toObservable()
                    }
            observable = observable!!
                    .doOnTerminate { fetchMap.remove(query) }
                    .doOnDispose { fetchMap.remove(query) }
                    .publish()
                    .refCount()
        }
        val finalObservable = observable
        observable = observable
                .doOnSubscribe { fetchMap[query] = finalObservable }
        return observable!!.ignoreElements()
    }

    private fun fetch(params: Params<Query, Entity>): Single<FetchResult<Entity>> {
        return fetcher.invoke(params)
    }

    class Builder<Query, Entity>(
            private val key: (Entity) -> Any,
            private val fetcher: ((Params<Query, Entity>) -> Single<FetchResult<Entity>>)
    ) {

        private var max = 50
        private var limit = 10
        private var cachePolicy: CachePolicy? = null

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

        fun build(): MemoryListStorage<Query, Entity> {
            if (cachePolicy == null) {
                cachePolicy = CachePolicy.infinite()
            }
            return MemoryListStorage(
                    max,
                    limit,
                    cachePolicy!!,
                    fetcher)
        }
    }

    data class Params<Query, Entity> internal constructor(
            var query: Query,
            var size: Int,
            var limit: Int,
            var entity: Entity?
    )

    data class FetchResult<E>(
            val data: List<E>,
            val hasNext: Boolean,
            val maxCount: Int = -1
    )


}

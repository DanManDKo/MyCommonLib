package com.movies.popular.popularmovies.data.common

import com.movies.popular.popularmovies.data.common.cashe.CachePolicy
import com.movies.popular.popularmovies.data.common.cashe.CachedEntry
import com.movies.popular.popularmovies.data.common.cashe.ObservableLruCache
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 2/25/18
 */
class MemoryStorage<Query, Entity> private
constructor(max: Int,
            private val cachePolicy: CachePolicy,
            private val fetcher: ((Query) -> Single<Entity>)?) {

    private val cache: ObservableLruCache<Query, CachedEntry<Entity>> = ObservableLruCache(max)

    private val updateSubject = PublishSubject.create<Query>()
    private val fetchMap = HashMap<Query, Observable<Entity>>()

    operator fun get(query: Query): Observable<Entity> {
        val objectObservable = cache[query]
                .filter { cachePolicy.test(it) }
                .map<Entity> { it.entry }
                .toObservable()
        return objectObservable.repeatWhen { updateSubject.filter { query == it } }
                .mergeWith(fetchIfExpired(query))
    }

    fun remove(query: Query): Completable {
        return Completable.fromAction {
            cache.remove(query)
            updateSubject.onNext(query)
        }
    }

    fun put(query: Query, entity: Entity): Completable {
        return Completable.fromAction {
            cache.put(query, CachePolicy.createEntry(entity))
            updateSubject.onNext(query)
        }
    }

    fun update(query: Query, onUpdateCallback: (Entity) -> Entity): Completable {
        return cache.get(query)
                .map<Entity> { it.entry }
                .doOnSuccess { entity ->
                    val newEntity = onUpdateCallback.invoke(entity)
                    cache.put(query, CachePolicy.createEntry(newEntity))
                    updateSubject.onNext(query)
                }
                .ignoreElement()
    }

    fun fetch(query: Query): Completable {
        if (fetcher != null) {
            var observable: Observable<Entity>? = fetchMap[query]
            if (observable == null) {
                observable = Observable.timer(1, TimeUnit.SECONDS)
                        .firstOrError()
                        .flatMap { fetcher.invoke(query) }
                        .toObservable()
                        .publish()
                        .refCount()
            }
            val finalObservable = observable
            return observable.firstOrError()
                    .doOnSuccess { entity ->
                        cache.put(query, CachePolicy.createEntry(entity))
                        updateSubject.onNext(query)
                    }
                    .doOnSubscribe { fetchMap[query] = finalObservable }
                    .doOnDispose { fetchMap.remove(query) }
                    .ignoreElement()
        } else {
            return Completable.complete()
        }
    }

    private fun fetchIfExpired(query: Query): Observable<Entity> {
        return cache[query]
                .map { cachedEntry -> !cachePolicy.test(cachedEntry) }
                .toSingle(true)
                .flatMapCompletable { expired ->
                    if (expired) {
                        fetch(query)
                    } else {
                        Completable.complete()
                    }
                }.toObservable()
    }

    class Builder<Query, Entity> {

        private var max: Int = 0
        private var fetcher: ((Query) -> Single<Entity>)? = null
        private var cachePolicy: CachePolicy? = null

        fun capacity(max: Int): Builder<Query, Entity> {
            this.max = max
            return this
        }

        fun fetcher(fetcher: (Query) -> Single<Entity>): Builder<Query, Entity> {
            this.fetcher = fetcher
            return this
        }

        fun cachePolicy(cachePolicy: CachePolicy): Builder<Query, Entity> {
            this.cachePolicy = cachePolicy
            return this
        }

        fun build(): MemoryStorage<Query, Entity> {
            this.max = if (this.max == 0) 10 else this.max
            this.cachePolicy = this.cachePolicy ?: CachePolicy.create(5, TimeUnit.MINUTES)
            if (fetcher == null) {
                this.cachePolicy = CachePolicy.infinite()
            }
            return MemoryStorage(max, cachePolicy!!, fetcher)
        }
    }

}

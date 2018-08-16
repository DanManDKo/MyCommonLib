package com.movies.popular.popularmovies.data.common

import com.movies.popular.popularmovies.data.common.cashe.CachePolicy
import com.movies.popular.popularmovies.data.common.cashe.CachedEntry
import com.movies.popular.popularmovies.data.common.cashe.ObservableLruCache
import com.movies.popular.popularmovies.data.common.cashe.Page
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 2/25/18
 */
class MemoryListStorage2<Query, Entity>
private constructor(max: Int,
                    private val cachePolicy: CachePolicy) {

    private val cache: ObservableLruCache<Query, CachedEntry<Page<Entity>>> = ObservableLruCache(max)

    private val updateSubject = PublishSubject.create<Query>()

    operator fun get(query: Query): Page<Entity> {
        return cache[query]
                .filter { cachePolicy.test(it) }
                .map<Page<Entity>> { it.entry }
                .blockingGet(Page(true))
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

    fun add(query: Query, newData: List<Entity>) {
        val page = get(query)
        page.addResult(newData)
        cache.put(query, CachePolicy.createEntry(page))
    }

    fun remove(query: Query): Completable {
        return Completable.fromAction { cache.clear(query) }
    }

    fun put(query: Query, data: List<Entity>) {
        val page = Page<Entity>(true)
        page.addResult(data)
        cache.put(query, CachePolicy.createEntry(page))
    }

    class Builder<Query, Entity>() {

        private var max = 50
        private var cachePolicy: CachePolicy? = null

        fun capacity(max: Int): Builder<Query, Entity> {
            this.max = max
            return this
        }

        fun cachePolicy(cachePolicy: CachePolicy): Builder<Query, Entity> {
            this.cachePolicy = cachePolicy
            return this
        }

        fun build(): MemoryListStorage2<Query, Entity> {
            if (cachePolicy == null) {
                cachePolicy = CachePolicy.infinite()
            }
            return MemoryListStorage2(
                    max,
                    cachePolicy!!)
        }
    }

}

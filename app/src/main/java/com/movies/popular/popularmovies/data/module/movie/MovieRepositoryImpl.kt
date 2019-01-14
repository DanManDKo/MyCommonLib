package com.movies.popular.popularmovies.data.module.movie

import androidx.paging.PagedList
import com.movies.popular.popularmovies.domain.model.Movie
import com.movies.popular.popularmovies.domain.repository.MovieRepository
import com.sprinklebit.library.data.common.MemoryDataSource
import com.sprinklebit.library.data.common.cashe.CachePolicy
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * User: Sasha Shcherbinin
 * Date : 7/16/18
 */
@Singleton
class MovieRepositoryImpl
@Inject
constructor(private val movieNetworkStorage: MovieNetworkStorage)
    : MovieRepository {

    private val dataSource = MemoryDataSource
            .Builder<Unit, Movie> { dataSource ->
                movieNetworkStorage.getMovies(dataSource.page)
                        .map { MemoryDataSource.FetchResult(it, it.isNotEmpty(), 0) }
            }
            .capacity(1)
            .cachePolicy(CachePolicy.create(5, TimeUnit.MINUTES))
            .prefetchDistance(5)
            .limit(10)
            .build()

    override fun getMovieList(): Observable<PagedList<Movie>> {
        return dataSource[Unit]
    }

    override fun observeLoading(): Observable<Boolean> {
        return dataSource.observeLoading(Unit)
    }

    override fun refresh(): Completable {
        return dataSource.refresh(Unit)
    }

    override fun updateMovie(id: Int): Completable {
        return dataSource.update({ it.id == id }, { movie ->
            movie.copy(overview = "", title = "")
        })
    }

    override fun remove(id: Int): Completable {
        return dataSource.remove{ it.id == id }
    }
}
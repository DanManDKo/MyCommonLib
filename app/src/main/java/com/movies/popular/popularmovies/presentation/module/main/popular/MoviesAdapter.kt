package com.movies.popular.popularmovies.presentation.module.main.popular

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.movies.popular.popularmovies.R
import com.movies.popular.popularmovies.databinding.ItemMovieBinding
import com.movies.popular.popularmovies.domain.model.Movie


/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/15/17
 * Time: 7:17 PM
 */
class MoviesAdapter(context: Context, private val viewModel: MoviesViewModel)
    : PagedListAdapter<Movie, MoviesAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemMovieBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie?) {
            binding.item = item
            binding.viewModel = viewModel
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: Movie, newConcert: Movie): Boolean {
                return oldConcert.id == newConcert.id
            }

            override fun areContentsTheSame(oldConcert: Movie,
                                            newConcert: Movie): Boolean {
                return oldConcert.equals(newConcert)
            }
        }
    }

}

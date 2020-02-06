package com.movies.popular.popularmovies.presentation.module.main.popular

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.movies.popular.popularmovies.R
import com.movies.popular.popularmovies.databinding.ItemMovieBinding
import com.movies.popular.popularmovies.domain.model.Movie

class MoviesAdapter(context: Context, private val viewModel: MoviesViewModel)
    : PagedListAdapter<Movie, MoviesAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemMovieBinding)
        : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie?) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
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

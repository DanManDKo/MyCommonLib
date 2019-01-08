package com.movies.popular.popularmovies.presentation.module.main.popular

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movies.popular.popularmovies.R
import com.movies.popular.popularmovies.databinding.FragmentMoviesBinding
import com.movies.popular.popularmovies.presentation.common.BaseFragment
import com.movies.popular.popularmovies.presentation.common.adapter.LoadingAdapter
import com.movies.popular.popularmovies.presentation.common.helper.UiHelper
import javax.inject.Inject

/**
 * User: Sasha Shcherbinin
 * Date : 5/4/18
 */
class MoviesFragment : BaseFragment() {

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var uiHelper: UiHelper

    lateinit var binding: FragmentMoviesBinding
    lateinit var adapter: MoviesAdapter
    lateinit var loadingAdapter: LoadingAdapter
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MoviesViewModel::class.java)

        adapter = MoviesAdapter(context!!, viewModel)
        loadingAdapter = LoadingAdapter(adapter)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_movies, container, false)
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.recyclerView.adapter = loadingAdapter
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        binding.swipeRl.setOnRefreshListener { viewModel.onRefresh() }
    }

    private fun initObservers() {
        viewModel.errorMessage.observe(this, Observer { uiHelper.showErrorToast(it) })
        viewModel.content.observe(this, Observer { adapter.submitList(it!!) })
        viewModel.itemClickedEvent.observe(this, Observer { })
        viewModel.refreshEvent.observe(this, Observer { binding.swipeRl.isRefreshing = it!! })
        viewModel.loading.observe(this, Observer { loadingAdapter.updateLoading(it!!) })
    }
}
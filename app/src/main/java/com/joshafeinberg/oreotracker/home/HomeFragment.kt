package com.joshafeinberg.oreotracker.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.util.observe

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerview: RecyclerView
    private lateinit var throwUpAdapter: ThrowUpAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = activity?.run {
            ViewModelProviders.of(this, SavedStateViewModelFactory(this)).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        recyclerview = view.findViewById(R.id.recyclerview)
        throwUpAdapter = ThrowUpAdapter()
        recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = throwUpAdapter
        }

        val loadingView = view.findViewById<ContentLoadingProgressBar>(R.id.loading)

        homeViewModel.state.observe(this) { state ->
            if (state.isLoading) {
                loadingView.show()
            } else {
                loadingView.hide()
            }

            throwUpAdapter.items = state.items
        }

        homeViewModel.downloadItems()
    }
}
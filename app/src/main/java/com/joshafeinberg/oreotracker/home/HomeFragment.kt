package com.joshafeinberg.oreotracker.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.databinding.FragmentHomeBinding
import com.joshafeinberg.oreotracker.util.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var throwUpAdapter: ThrowUpAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        throwUpAdapter = ThrowUpAdapter()
        binding.recyclerview.apply {
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
    }
}
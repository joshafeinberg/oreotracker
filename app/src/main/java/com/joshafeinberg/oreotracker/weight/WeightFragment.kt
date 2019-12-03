package com.joshafeinberg.oreotracker.weight

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.arch.util.observe

class WeightFragment : Fragment(R.layout.fragment_weight) {

    private val weightViewModel: WeightViewModel by activityViewModels()
    private lateinit var recyclerview: RecyclerView
    private lateinit var weightAdapter: WeightAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview = view.findViewById(R.id.recyclerview)
        weightAdapter = WeightAdapter()
        recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weightAdapter
        }

        val loadingView = view.findViewById<ContentLoadingProgressBar>(R.id.loading)

        weightViewModel.state.observe(this) { state ->
            if (state.isLoading) {
                loadingView.show()
            } else {
                loadingView.hide()
            }

            weightAdapter.items = state.items
        }

        weightViewModel.downloadItems()
    }

}

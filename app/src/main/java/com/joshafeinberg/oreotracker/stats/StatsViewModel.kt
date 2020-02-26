package com.joshafeinberg.oreotracker.stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.StateViewModel
import com.joshafeinberg.oreotracker.network.NetworkModule
import kotlinx.coroutines.launch

class StatsViewModel(override val savedState: SavedStateHandle) : ViewModel(), StateViewModel<StatsViewState> {

    override val initialState: StatsViewState = StatsViewState()

    init {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getStats()
            updateState { setStats(items) }
        }
    }
}

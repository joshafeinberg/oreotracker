package com.joshafeinberg.oreotracker.stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.arch.ViewStateManager
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class StatsViewModel(savedStateHandle: SavedStateHandle) : ViewStateManager.ViewStateViewModel<StatsViewModel.StatsState, StatsViewModel.StatsEvents>(savedStateHandle) {

    override val initialState: StatsViewModel.StatsState = StatsState()

    fun downloadStats() {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getStats()
            updateState { setStats(items) }
        }
    }

    @Parcelize
    data class StatsState(val isLoading: Boolean = true, val stats: Stats? = null) : ViewState {
        fun setStats(items: Stats): StatsState = copy(isLoading = false, stats =  items)
    }

    sealed class StatsEvents : ViewEvents
}

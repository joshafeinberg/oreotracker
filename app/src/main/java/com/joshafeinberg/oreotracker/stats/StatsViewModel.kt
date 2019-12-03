package com.joshafeinberg.oreotracker.stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.SavedStateViewModel
import com.joshafeinberg.oreotracker.arch.StateViewModel
import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class StatsViewModel(override val savedState: SavedStateHandle) : ViewModel(), StateViewModel<StatsViewModel.StatsState> {

    override val initialState: StatsState = StatsState()

    init {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getStats()
            updateState { setStats(items) }
        }
    }

    @Parcelize
    data class StatsState(val isLoading: Boolean = true, val stats: Stats? = null) : ViewState {
        fun setStats(items: Stats): StatsState = copy(isLoading = false, stats =  items)
    }

}

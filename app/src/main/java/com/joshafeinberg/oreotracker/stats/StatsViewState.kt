package com.joshafeinberg.oreotracker.stats

import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatsViewState(val isLoading: Boolean = true, val stats: Stats? = null) : ViewState {
    fun setStats(items: Stats): StatsViewState = copy(isLoading = false, stats = items)
}

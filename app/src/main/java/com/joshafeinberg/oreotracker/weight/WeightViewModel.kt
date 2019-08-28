package com.joshafeinberg.oreotracker.weight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.arch.ViewStateManager
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class WeightViewModel(savedState: SavedStateHandle) : ViewStateManager.ViewStateViewModel<WeightViewModel.WeightState, WeightViewModel.WeightEvents>(savedState) {

    override val initialState: WeightState = WeightState()

    fun downloadItems() {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getWeights()
            updateState { setItems(items) }
        }
    }

    fun addWeight(weight: Weight) {
        updateState { addItem(weight) }
    }

    @Parcelize
    data class WeightState(val isLoading: Boolean = true, val items: List<Weight> = emptyList()) : ViewState {
        fun setItems(items: List<Weight>): WeightState = copy(isLoading = false, items = items)
        fun addItem(item: Weight): WeightState = copy(isLoading = false, items = items.toMutableList().apply { add(0, item) }.toList())
    }

    sealed class WeightEvents : ViewEvents
}

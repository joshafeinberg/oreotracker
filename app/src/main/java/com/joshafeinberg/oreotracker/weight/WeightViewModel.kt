package com.joshafeinberg.oreotracker.weight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.SavedStateViewModel
import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class WeightViewModel(override val savedState: SavedStateHandle) : ViewModel(), SavedStateViewModel<WeightViewModel.WeightState, WeightViewModel.WeightEvents> {

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

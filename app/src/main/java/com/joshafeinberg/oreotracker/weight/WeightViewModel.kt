package com.joshafeinberg.oreotracker.weight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.StateViewModel
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.coroutines.launch

class WeightViewModel(override val savedState: SavedStateHandle) : ViewModel(), StateViewModel<WeightViewState> {

    override val initialState: WeightViewState = WeightViewState()

    init {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getWeights()
            updateState { setItems(items) }
        }
    }

    fun addWeight(weight: Weight) {
        updateState { addItem(weight) }
    }
}

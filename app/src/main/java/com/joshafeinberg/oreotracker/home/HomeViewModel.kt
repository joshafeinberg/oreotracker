package com.joshafeinberg.oreotracker.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.StateViewModel
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import kotlinx.coroutines.launch

class HomeViewModel(override val savedState: SavedStateHandle) : ViewModel(), StateViewModel<HomeViewState> {

    override val initialState: HomeViewState = HomeViewState()

    init {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getThrowUps()
            updateState { setItems(items) }
        }
    }

    fun addItem(throwUp: ThrowUp) {
        updateState { addItem(throwUp) }
    }
}

package com.joshafeinberg.oreotracker.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.arch.ViewStateManager
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class HomeViewModel(savedState: SavedStateHandle) : ViewStateManager.ViewStateViewModel<HomeViewModel.HomeState, HomeViewModel.HomeEvents>(savedState) {

    override val initialState: HomeState = HomeState()

    fun downloadItems() {
        viewModelScope.launch {
            val items = NetworkModule.adapter.getThrowUps()
            updateState { setItems(items) }
        }
    }

    fun addItem(throwUp: ThrowUp) {
        updateState { addItem(throwUp) }
    }

    @Parcelize
    data class HomeState(val isLoading: Boolean = true, val items: List<ThrowUp> = emptyList()) : ViewState {
        fun setItems(items: List<ThrowUp>): HomeState = copy(isLoading = false, items = items)
        fun addItem(item: ThrowUp): HomeState = copy(isLoading = false, items = items.toMutableList().apply { add(0, item) }.toList())
    }

    sealed class HomeEvents : ViewEvents
}

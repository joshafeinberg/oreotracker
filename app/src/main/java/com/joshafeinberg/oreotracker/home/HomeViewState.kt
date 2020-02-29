package com.joshafeinberg.oreotracker.home

import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeViewState(val isLoading: Boolean = true, val items: List<ThrowUp> = emptyList()) : ViewState {
    fun setItems(items: List<ThrowUp>): HomeViewState = copy(isLoading = false, items = items)
    fun addItem(item: ThrowUp): HomeViewState = copy(isLoading = false, items = items.toMutableList().apply { add(0, item) }.toList())
}

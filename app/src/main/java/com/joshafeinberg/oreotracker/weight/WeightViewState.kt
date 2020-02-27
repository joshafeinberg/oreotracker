package com.joshafeinberg.oreotracker.weight

import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeightViewState(val isLoading: Boolean = true, val items: List<Weight> = emptyList()) : ViewState {
    fun setItems(items: List<Weight>): WeightViewState = copy(isLoading = false, items = items)
    fun addItem(item: Weight): WeightViewState = copy(isLoading = false, items = items.toMutableList().apply { add(0, item) }.toList())
}

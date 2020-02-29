package com.joshafeinberg.oreotracker.weight.add

import com.joshafeinberg.oreotracker.arch.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddWeightViewState(
    val selectedDate: Long = System.currentTimeMillis()
) : ViewState {
    fun setSelectedDate(selectedDate: Long): AddWeightViewState = copy(selectedDate = selectedDate)
}

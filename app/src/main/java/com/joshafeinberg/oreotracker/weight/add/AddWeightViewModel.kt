package com.joshafeinberg.oreotracker.weight.add

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

class AddWeightViewModel(override val savedState: SavedStateHandle) : ViewModel(), SavedStateViewModel<AddWeightViewModel.AddViewState, AddWeightViewModel.AddViewEvents> {
    override val initialState = AddViewState()

    private var selectedDate: Long = System.currentTimeMillis()

    fun onDateSelected(selectedDate: Long) {
        this.selectedDate = selectedDate
        updateState { setSelectedDate(selectedDate) }
    }

    fun onSaveClicked(myWeight: String?, ourWeight: String?) {
        if (myWeight.isNullOrEmpty() || ourWeight.isNullOrEmpty()) {
            return
        }
        val myWeightFloat = myWeight.toFloatOrNull() ?: return
        val ourWeightFloat = ourWeight.toFloatOrNull() ?: return
        val finalWeight = Weight(selectedDate, ourWeightFloat - myWeightFloat)
        emitEvent(AddViewEvents.Saving)
        viewModelScope.launch {
            NetworkModule.adapter.postWeight(finalWeight)
            emitEvent(AddViewEvents.WeightSaved(finalWeight))
        }
    }

    @Parcelize
    data class AddViewState(
            val selectedDate: Long = System.currentTimeMillis()
    ) : ViewState {
        fun setSelectedDate(selectedDate: Long): AddViewState = copy(selectedDate = selectedDate)
    }

    sealed class AddViewEvents : ViewEvents {
        object Saving : AddViewEvents()
        data class WeightSaved(val weight: Weight) : AddViewEvents()
    }

}

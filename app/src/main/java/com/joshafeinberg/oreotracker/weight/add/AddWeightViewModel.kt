package com.joshafeinberg.oreotracker.weight.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.SavedStateViewModel
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.coroutines.launch

class AddWeightViewModel(override val savedState: SavedStateHandle) : ViewModel(), SavedStateViewModel<AddWeightViewState, AddWeightEvents> {
    override val initialState = AddWeightViewState()

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
        emitEvent(AddWeightEvents.Saving)
        viewModelScope.launch {
            NetworkModule.adapter.postWeight(finalWeight)
            emitEvent(AddWeightEvents.WeightSaved(finalWeight))
        }
    }

    fun onDatePickerSelected() {
        emitEvent(AddWeightEvents.ShowDatePicker(selectedDate))
    }
}

package com.joshafeinberg.oreotracker.weight.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.add.AddEvents
import com.joshafeinberg.oreotracker.arch.SavedStateViewModel
import com.joshafeinberg.oreotracker.arch.SingleLiveEvent
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddWeightViewModel(override val savedState: SavedStateHandle) : ViewModel(), SavedStateViewModel<AddWeightViewState, AddWeightEvents> {
    override val initialState = AddWeightViewState()
    private val _events = SingleLiveEvent<AddWeightEvents>()
    val events: LiveData<AddWeightEvents> = _events

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
            delay(5000)
            //NetworkModule.adapter.postWeight(finalWeight)
            emitEvent(AddWeightEvents.WeightSaved(finalWeight))
        }
    }

    private fun emitEvent(event: AddWeightEvents) {
        _events.value = event
    }
}

package com.joshafeinberg.oreotracker.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshafeinberg.oreotracker.arch.SavedStateViewModel
import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.network.NetworkModule
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Time
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class AddViewModel(override val savedState: SavedStateHandle) : ViewModel(),
        SavedStateViewModel<AddViewModel.AddViewState, AddViewModel.AddViewEvents> {
    override val initialState = AddViewState()

    private var selectedDate: Long = System.currentTimeMillis()
    private var selectedTime: Time? = null
    private var selectedContent: Content? = null

    fun onDateSelected(selectedDate: Long) {
        this.selectedDate = selectedDate
        updateState { setSelectedDate(selectedDate) }
    }

    fun onTimeSelected(kClass: KClass<out Time>) {
        if (kClass.simpleName == Time.ExactTime::class.simpleName) {
            updateState { toggleTimeField(true) }
        } else {
            this.selectedTime = kClass.objectInstance
            updateState { setSelectedTime(kClass.objectInstance!!) }
        }
    }

    fun onExactTimeSelected(selectedTime: Long) {
        this.selectedTime = Time.ExactTime(selectedTime).also {
            updateState { setSelectedTime(it) }
        }
    }

    fun onContentSelected(content: Content) {
        this.selectedContent = content.also {
            updateState { setSelectedContent(it) }
        }
    }

    fun onSaveClicked() {
        val finalThrowUp = ThrowUp(selectedDate, selectedTime!!, selectedContent!!)
        emitEvent(AddViewEvents.Saving)
        viewModelScope.launch {
            NetworkModule.adapter.postThrowUp(finalThrowUp)
            emitEvent(AddViewEvents.ThrowUpSaved(finalThrowUp))
        }
    }

    @Parcelize
    data class AddViewState(
            val selectedDate: Long = System.currentTimeMillis(),
            val selectedTime: Time? = null,
            val timeFieldVisible: Boolean = false,
            val selectedContent: Content? = null
    ) : ViewState {
        fun setSelectedDate(selectedDate: Long): AddViewState = copy(selectedDate = selectedDate)
        fun toggleTimeField(showTimeField: Boolean) = copy(selectedTime = null, timeFieldVisible = showTimeField)
        fun setSelectedTime(selectedTime: Time) = copy(selectedTime = selectedTime, timeFieldVisible = selectedTime is Time.ExactTime)
        fun setSelectedContent(selectedContent: Content) = copy(selectedContent = selectedContent)
    }

    sealed class AddViewEvents : ViewEvents {
        object Saving : AddViewEvents()
        data class ThrowUpSaved(val throwUp: ThrowUp) : AddViewEvents()
    }

}

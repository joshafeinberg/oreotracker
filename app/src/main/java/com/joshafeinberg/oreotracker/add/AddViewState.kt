package com.joshafeinberg.oreotracker.add

import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Time
import kotlinx.android.parcel.Parcelize

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
package com.joshafeinberg.oreotracker.add

import android.os.Parcel
import android.os.Parcelable
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Time

data class AddViewState(
    val selectedDate: Long = System.currentTimeMillis(),
    val selectedTime: Time? = null,
    val timeFieldVisible: Boolean = false,
    val selectedContent: Content? = null
) : ViewState {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readSerializable() as Time?,
            parcel.readByte() != 0.toByte(),
            parcel.readSerializable() as Content?)

    fun setSelectedDate(selectedDate: Long): AddViewState = copy(selectedDate = selectedDate)
    fun toggleTimeField(showTimeField: Boolean) = copy(selectedTime = null, timeFieldVisible = showTimeField)
    fun setSelectedTime(selectedTime: Time) = copy(selectedTime = selectedTime, timeFieldVisible = selectedTime is Time.ExactTime)
    fun setSelectedContent(selectedContent: Content) = copy(selectedContent = selectedContent)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(selectedDate)
        parcel.writeSerializable(selectedTime)
        parcel.writeByte(if (timeFieldVisible) 1 else 0)
        parcel.writeSerializable(selectedContent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddViewState> {
        override fun createFromParcel(parcel: Parcel): AddViewState {
            return AddViewState(parcel)
        }

        override fun newArray(size: Int): Array<AddViewState?> {
            return arrayOfNulls(size)
        }
    }
}

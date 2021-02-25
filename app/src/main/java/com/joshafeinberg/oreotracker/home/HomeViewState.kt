package com.joshafeinberg.oreotracker.home

import android.os.Parcel
import android.os.Parcelable
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import kotlinx.android.parcel.Parcelize

data class HomeViewState(val isLoading: Boolean = true, val items: List<ThrowUp> = emptyList()) : ViewState {
    fun setItems(items: List<ThrowUp>): HomeViewState = copy(isLoading = false, items = items)
    fun addItem(item: ThrowUp): HomeViewState = copy(isLoading = false, items = items.toMutableList().apply { add(0, item) }.toList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isLoading) 1 else 0)
        parcel.writeList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeViewState> {
        override fun createFromParcel(parcel: Parcel): HomeViewState {
            val isLoading = parcel.readByte() != 0.toByte()
            val items: Array<ThrowUp>? = parcel.readArrayList(ThrowUp::class.java.classLoader) as Array<ThrowUp>?
            return HomeViewState(
                    isLoading,
                    items?.toList() ?: emptyList()
            )
        }

        override fun newArray(size: Int): Array<HomeViewState?> {
            return arrayOfNulls(size)
        }
    }
}

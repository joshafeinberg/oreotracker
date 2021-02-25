package com.joshafeinberg.oreotracker.weight

import android.os.Parcel
import android.os.Parcelable
import com.joshafeinberg.oreotracker.arch.ViewState
import com.joshafeinberg.oreotracker.home.HomeViewState
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import kotlinx.android.parcel.Parcelize

data class WeightViewState(val isLoading: Boolean = true, val items: List<Weight> = emptyList()) : ViewState {

    fun setItems(items: List<Weight>): WeightViewState = copy(isLoading = false, items = items)
    fun addItem(item: Weight): WeightViewState = copy(isLoading = false, items = items.toMutableList().apply { add(0, item) }.toList())
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isLoading) 1 else 0)
        parcel.writeList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeightViewState> {
        override fun createFromParcel(parcel: Parcel): WeightViewState {
            val isLoading = parcel.readByte() != 0.toByte()
            val items: Array<Weight>? = parcel.readArrayList(Weight::class.java.classLoader) as Array<Weight>?
            return WeightViewState(
                    isLoading,
                    items?.toList() ?: emptyList()
            )
        }

        override fun newArray(size: Int): Array<WeightViewState?> {
            return arrayOfNulls(size)
        }
    }
}

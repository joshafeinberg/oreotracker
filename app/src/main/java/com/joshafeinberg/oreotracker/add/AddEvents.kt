package com.joshafeinberg.oreotracker.add

import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp

sealed class AddEvents : ViewEvents {
    object Saving : AddEvents()
    data class ThrowUpSaved(val throwUp: ThrowUp) : AddEvents()
    data class ShowDatePicker(val selectedDate: Long) : AddEvents()
}
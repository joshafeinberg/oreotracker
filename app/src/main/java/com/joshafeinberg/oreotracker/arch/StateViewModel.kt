package com.joshafeinberg.oreotracker.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle

interface StateViewModel<V : ViewState> {
    val savedState: SavedStateHandle
    val initialState: V

    fun updateState(block: V.() -> V){
        val oldState = state.value!! // state has an initial value so this can literally never be null
        val newState = oldState.block()
        savedState.set(STATE, newState)
    }
}

val <V : ViewState> StateViewModel<V>.state: LiveData<V>
    get() = savedState.getLiveData(STATE, initialState)

private const val STATE = "state"
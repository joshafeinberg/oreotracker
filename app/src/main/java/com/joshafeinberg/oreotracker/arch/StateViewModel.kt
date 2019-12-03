package com.joshafeinberg.oreotracker.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

interface StateViewModel<V : ViewState> {
    val savedState: SavedStateHandle
    val initialState: V

    fun updateState(block: V.() -> V){
        val newState = viewState.block()
        savedState.set(STATE, newState)
        _state.value = newState
    }
}

private val <V: ViewState> StateViewModel<V>.viewState: V
    get() = _state.value ?: savedState.get(STATE) ?: initialState

private val <V: ViewState> StateViewModel<V>._state: MutableLiveData<V> by lazy {
    MutableLiveData<V>()
}

val <V : ViewState> StateViewModel<V>.state: LiveData<V>
    get() = _state

private const val STATE = "state"
package com.joshafeinberg.oreotracker.arch

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ViewStateManager<V : ViewState>(initialViewState: V) {
    var viewState: V = initialViewState
        private set

    abstract class ViewStateViewModel<V : ViewState, E : ViewEvents>(private val savedState: SavedStateHandle) : ViewModel() {

        private val viewStateManager by lazy {
            ViewStateManager(savedState.get("state") ?: initialState)
        }

        abstract val initialState: V

        fun updateState(block: V.() -> V){
            viewStateManager.viewState = viewStateManager.viewState.block()
            savedState.set("state", viewStateManager.viewState)
            (state as MutableLiveData).postValue(viewStateManager.viewState)
        }

        fun emitEvent(event: E) {
            (events as MutableLiveData).postValue(event)
        }

        val state: LiveData<V> = MutableLiveData()
        val events: LiveData<E> = SingleLiveEvent()
    }
}

interface ViewState : Parcelable
interface ViewEvents
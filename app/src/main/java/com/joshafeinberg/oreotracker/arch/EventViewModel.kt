package com.joshafeinberg.oreotracker.arch

import androidx.lifecycle.LiveData

interface EventViewModel<E : ViewEvents> {
    /*fun emitEvent(event: E) {
        _events.value = event
    }*/
}

/*private val <E : ViewEvents> EventViewModel<E>._events: SingleLiveEvent<E> by lazy {
    SingleLiveEvent<E>()
}
val <E : ViewEvents> EventViewModel<E>.events: LiveData<E>
    get() = _events*/

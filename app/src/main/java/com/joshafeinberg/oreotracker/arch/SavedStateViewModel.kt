package com.joshafeinberg.oreotracker.arch

interface SavedStateViewModel<V : ViewState, E : ViewEvents> : StateViewModel<V>, EventViewModel<E>

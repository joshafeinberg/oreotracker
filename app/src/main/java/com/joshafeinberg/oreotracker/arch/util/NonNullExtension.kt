package com.joshafeinberg.oreotracker.arch.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, nonNullListener: (T) -> Unit) {
    observe(lifecycleOwner, Observer {
        if (it != null) {
            nonNullListener(it)
        }
    })
}

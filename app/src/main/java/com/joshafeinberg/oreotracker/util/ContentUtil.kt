package com.joshafeinberg.oreotracker.util

import com.joshafeinberg.oreotracker.sharedmodule.Content

val Content.readableName: String
    get() {
        return name.toLowerCase().capitalize()
    }
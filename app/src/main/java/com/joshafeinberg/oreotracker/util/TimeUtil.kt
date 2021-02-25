package com.joshafeinberg.oreotracker.util

val <T> Class<T>.readableName: String
    get() {
        var simpleName = simpleName
        val position = simpleName.substring(1).indexOfFirst { Character.isUpperCase(it) } + 1
        if (position > 0) {
            simpleName = simpleName.substring(0, position) + " " + simpleName.substring(position)
        }
        return simpleName
    }

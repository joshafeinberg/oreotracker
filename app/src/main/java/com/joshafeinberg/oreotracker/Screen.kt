package com.joshafeinberg.oreotracker

import androidx.annotation.StringRes

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddSickness : Screen("add_sickness")
    object AddWeight : Screen("add_weight")
}

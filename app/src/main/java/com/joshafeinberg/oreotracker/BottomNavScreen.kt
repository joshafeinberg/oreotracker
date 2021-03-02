package com.joshafeinberg.oreotracker

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

sealed class BottomNavScreen(val route: String, @StringRes val resourceId: Int, val image: Any) {
    object Home : BottomNavScreen("home", R.string.home, Icons.Filled.Home)
    object Statistics : BottomNavScreen("stats", R.string.statistics, R.drawable.ic_equalizer_black_24dp)
    object Weight : BottomNavScreen("weight", R.string.weight, R.drawable.ic_pets_black_24dp)
}

val bottomNavScreens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Statistics,
        BottomNavScreen.Weight
)
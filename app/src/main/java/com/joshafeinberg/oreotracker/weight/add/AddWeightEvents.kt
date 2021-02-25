package com.joshafeinberg.oreotracker.weight.add

import com.joshafeinberg.oreotracker.arch.ViewEvents
import com.joshafeinberg.oreotracker.sharedmodule.Weight

sealed class AddWeightEvents : ViewEvents {
    object Saving : AddWeightEvents()
    data class WeightSaved(val weight: Weight) : AddWeightEvents()
}

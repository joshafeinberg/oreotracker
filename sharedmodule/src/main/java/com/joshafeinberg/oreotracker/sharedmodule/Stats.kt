package com.joshafeinberg.oreotracker.sharedmodule

import java.io.Serializable

data class Stats(
    val lastThirtyDayCount: Int,
    val lastThirtyDayTimeCount: Map<String, Int>,
    val lastThirtyDayContentCount: Map<String, Int>,

    val lastSevenDayCount: Int,
    val lastSevenDayTimeCount: Map<String, Int>,
    val lastSevenDayContentCount: Map<String, Int>
) : Serializable

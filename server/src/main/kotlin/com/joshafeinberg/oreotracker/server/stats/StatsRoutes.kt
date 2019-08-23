package com.joshafeinberg.oreotracker.server.stats

import com.joshafeinberg.oreotracker.server.di.TypedRoute
import com.joshafeinberg.oreotracker.sharedmodule.STATS_GET
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

object StatsRoutes {

    @KtorExperimentalLocationsAPI
    @Location("/$STATS_GET")
    object Stats : TypedRoute

}
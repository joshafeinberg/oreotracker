package com.joshafeinberg.oreotracker.server.stats

import com.joshafeinberg.oreotracker.server.di.TypedRoute
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

object StatsRoutes {

    @KtorExperimentalLocationsAPI
    @Location("/stats")
    object Stats : TypedRoute

}
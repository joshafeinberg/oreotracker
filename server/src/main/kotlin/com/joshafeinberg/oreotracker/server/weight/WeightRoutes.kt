package com.joshafeinberg.oreotracker.server.weight

import com.joshafeinberg.oreotracker.server.di.TypedRoute
import com.joshafeinberg.oreotracker.sharedmodule.SICKNESS_GET
import com.joshafeinberg.oreotracker.sharedmodule.SICKNESS_POST
import com.joshafeinberg.oreotracker.sharedmodule.WEIGHT_GET
import com.joshafeinberg.oreotracker.sharedmodule.WEIGHT_POST
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@KtorExperimentalLocationsAPI
object WeightRoutes {
    @Location("/$WEIGHT_GET")
    object Weight : TypedRoute

    @Location("/$WEIGHT_POST")
    object AddWeight : TypedRoute
}

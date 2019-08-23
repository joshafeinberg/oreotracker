package com.joshafeinberg.oreotracker.server.tracker

import com.joshafeinberg.oreotracker.server.di.TypedRoute
import com.joshafeinberg.oreotracker.sharedmodule.SICKNESS_GET
import com.joshafeinberg.oreotracker.sharedmodule.SICKNESS_POST
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@KtorExperimentalLocationsAPI
object OreoRoutes {
    @Location("/$SICKNESS_GET")
    object ThrowUp : TypedRoute

    @Location("/$SICKNESS_POST")
    object AddThrowUp : TypedRoute
}

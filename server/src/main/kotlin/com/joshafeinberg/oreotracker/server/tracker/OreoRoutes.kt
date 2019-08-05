package com.joshafeinberg.oreotracker.server.tracker

import com.joshafeinberg.oreotracker.server.di.TypedRoute
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@KtorExperimentalLocationsAPI
object OreoRoutes {
    @Location("/throwup")
    object ThrowUp : TypedRoute

    @Location("/throwup")
    object AddThrowUp : TypedRoute
}

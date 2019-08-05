package com.joshafeinberg.oreotracker.server.di

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.locations
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
* A [KodeinAware] base class for Controllers handling routes.
* It allows to easily get dependencies, and offers some useful extensions like getting the [href] of a [TypedRoute].
*/
abstract class KodeinController(override val kodein: Kodein) : KodeinAware {
    /**
     * Injected dependency with the current [Application].
     */
    val application: Application by instance()

    /**
     * Shortcut to get the url of a [TypedRoute].
     */
    @KtorExperimentalLocationsAPI
    val TypedRoute.href get() = application.locations.href(this)

    /**
     * Method that subtypes must override to register the handled [Routing] routes.
     */
    abstract fun Routing.registerRoutes()
}
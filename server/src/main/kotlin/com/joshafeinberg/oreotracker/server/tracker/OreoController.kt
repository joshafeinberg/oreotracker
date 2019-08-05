package com.joshafeinberg.oreotracker.server.tracker

import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class OreoController(kodein: Kodein) : KodeinController(kodein) {

    val repository: OreoRepository by instance()

    override fun Routing.registerRoutes() {
        /**
         * GET route for [Routes.Users] /users, it responds
         * with a HTML listing all the users in the repository.
         */
        get<OreoRoutes.ThrowUp> {
            call.respond(repository.list())
        }

        post<OreoRoutes.AddThrowUp> {
            val throwUp = call.receive<ThrowUp>()

            repository.addThrowUp(throwUp)

            call.respond(HttpStatusCode.OK, throwUp)
        }
    }
}
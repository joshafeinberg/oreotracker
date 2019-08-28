package com.joshafeinberg.oreotracker.server.weight

import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class WeightController(kodein: Kodein) : KodeinController(kodein) {

    val repository: WeightRepository by instance()

    override fun Routing.registerRoutes() {
        /**
         * GET route for [Routes.Users] /users, it responds
         * with a HTML listing all the users in the repository.
         */
        get<WeightRoutes.Weight> {
            call.respond(repository.list())
        }

        post<WeightRoutes.AddWeight> {
            val weight = call.receive<Weight>()

            repository.addWeight(weight)

            call.respond(HttpStatusCode.OK, weight)
        }
    }
}
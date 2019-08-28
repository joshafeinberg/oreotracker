package com.joshafeinberg.oreotracker.server

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyService
import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.server.di.bindSingleton
import com.joshafeinberg.oreotracker.server.stats.StatsController
import com.joshafeinberg.oreotracker.server.stats.StatsRepository
import com.joshafeinberg.oreotracker.server.tracker.OreoController
import com.joshafeinberg.oreotracker.server.tracker.OreoRepository
import com.joshafeinberg.oreotracker.server.weight.WeightController
import com.joshafeinberg.oreotracker.server.weight.WeightRepository
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.routing
import org.kodein.di.Instance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.jvmType
import org.slf4j.event.Level

// Entry Point of the application as defined in resources/application.conf.
// @see https://ktor.io/servers/configuration.html#hocon-file
@Suppress("unused")
@KtorExperimentalLocationsAPI
fun Application.main() {
    kodeinApplication {

        install(ContentNegotiation) {
            gson {
                registerTypeAdapter(Time::class.java, Time.TimeAdapter())
                registerTypeAdapter(Content::class.java, Content.ContentAdapter())
            }
        }

        install(CallLogging) {
            level = Level.INFO
            filter { call -> call.request.path().startsWith("/") }
        }

        install(StatusPages) {
            exception<Throwable> { cause ->
                cause.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, cause.message!!)
                throw cause
            }
        }

        // todo - make debug flags
        /*ObjectifyService.init(ObjectifyFactory(
                DatastoreOptions.newBuilder()
                        .setHost("http://localhost:8484")
                        .setProjectId("oreo-tracker")
                        .build()
                        .service
                )
        )*/
        ObjectifyService.init()
        ObjectifyService.register(ThrowUp::class.java)
        ObjectifyService.register(Time.Overnight::class.java)
        ObjectifyService.register(Time.BetweenMeals::class.java)
        ObjectifyService.register(Time.AfterDinner::class.java)
        ObjectifyService.register(Time.ExactTime::class.java)
        ObjectifyService.register(Content::class.java)
        ObjectifyService.register(Weight::class.java)

        bind<Objectify>() with provider { ObjectifyService.ofy() }
        bind<OreoRepository>() with singleton { OreoRepository(instance()) }
        bindSingleton { OreoController(it) }
        bind<StatsRepository>() with singleton { StatsRepository(instance()) }
        bindSingleton { StatsController(it) }
        bind<WeightRepository>() with singleton { WeightRepository(instance()) }
        bindSingleton { WeightController(it) }
    }
}

/**
* Registers a [kodeinApplication] that that will call [kodeinMapper] for mapping stuff.
* The [kodeinMapper] is a lambda that is in charge of mapping all the required.
*
* After calling [kodeinMapper], this function will search
* for registered subclasses of [KodeinController], and will call their [KodeinController.registerRoutes] methods.
*/
@KtorExperimentalLocationsAPI
fun Application.kodeinApplication(
        kodeinMapper: Kodein.MainBuilder.(Application) -> Unit = {}
) {
    val application = this

    // Allows to use classes annotated with @Location to represent URLs.
    // They are typed, can be constructed to generate URLs, and can be used to register routes.
    application.install(Locations)

    /**
     * Creates a [Kodein] instance, binding the [Application] instance.
     */
    val kodein = Kodein {
        bind<Application>() with instance(application)
        kodeinMapper(this, application)
    }

    /**
     * Detects all the registered [KodeinController] and registers its routes.
     */
    routing {
        for (bind in kodein.container.tree.bindings) {
            val bindClass = bind.key.type.jvmType as? Class<*>?
            if (bindClass != null && KodeinController::class.java.isAssignableFrom(bindClass)) {
                val res by kodein.Instance(bind.key.type)
                println("Registering '$res' routes...")
                (res as KodeinController).apply { registerRoutes() }
            }
        }
    }
}
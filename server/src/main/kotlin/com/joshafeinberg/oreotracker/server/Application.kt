package com.joshafeinberg.oreotracker.server

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyService
import com.joshafeinberg.oreotracker.server.di.bindSingleton
import com.joshafeinberg.oreotracker.server.stats.StatsController
import com.joshafeinberg.oreotracker.server.stats.StatsRepository
import com.joshafeinberg.oreotracker.server.tracker.SicknessController
import com.joshafeinberg.oreotracker.server.tracker.SicknessRepository
import com.joshafeinberg.oreotracker.server.weight.WeightController
import com.joshafeinberg.oreotracker.server.weight.WeightRepository
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.SicknessService
import com.joshafeinberg.oreotracker.sharedmodule.StatsService
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import com.joshafeinberg.oreotracker.sharedmodule.WeightService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.slf4j.event.Level

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
        bind<SicknessRepository>() with singleton { SicknessRepository(instance()) }
        bindSingleton { SicknessController(it) }
        bind<StatsRepository>() with singleton { StatsRepository(instance()) }
        bindSingleton { StatsController(it) }
        bind<WeightRepository>() with singleton { WeightRepository(instance()) }
        bindSingleton { WeightController(it) }
    }
}

@KtorExperimentalLocationsAPI
fun Application.kodeinApplication(
    kodeinMapper: Kodein.MainBuilder.(Application) -> Unit = {}
) {
    val application = this

    val kodein = Kodein {
        bind<Application>() with instance(application)
        kodeinMapper(this, application)
    }

    routing {
        val sicknessService: SicknessService = findService(kodein)
        val weightService: WeightService = findService(kodein)
        val statsService: StatsService = findService(kodein)

        retrofitService(service = Services(
            sicknessService = sicknessService,
            weightService = weightService,
            statsService = statsService
        ))
    }
}

private inline fun <reified S : Any> findService(kodein: Kodein): S {
    val service: S by kodein.instance()
    return service
}

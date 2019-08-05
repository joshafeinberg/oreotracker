package com.joshafeinberg.oreotracker.server.stats

import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class StatsController(kodein: Kodein) : KodeinController(kodein) {

    val repository: StatsRepository by instance()

    override fun Routing.registerRoutes() {
        get<StatsRoutes.Stats> {

            val list = repository.list()
            
            val lastThirtyDays = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
            val lastSevenDays = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)

            /*val defaultTimes = listOf(
                    Time.Overnight::class.java.simpleName to 0,
                    Time.BetweenMeals::class.java.simpleName to 0,
                    Time.AfterDinner::class.java.simpleName to 0,
                    Time.ExactTime::class.java.simpleName to 0
            )

            val defaultContents = listOf(
                    Content.LIQUID.name to 0,
                    Content.CHUNKY.name to 0,
                    Content.GRANULAR.name to 0
            )*/

            var lastThirtyDayCount = 0
            val lastThirtyDayTimeCount = mutableMapOf<String, Int>()
            val lastThirtyDayContentCount = mutableMapOf<String, Int>()
            var lastSevenDayCount = 0
            val lastSevenDayTimeCount = mutableMapOf<String, Int>()
            val lastSevenDayContentCount = mutableMapOf<String, Int>()
            
            for (throwUp in list) {
                val timeKey = throwUp.time::class.java.simpleName.toLowerCase()
                val contentKey = throwUp.content.name.toLowerCase()
                if (throwUp.date >= lastThirtyDays) {
                    val timeCount = lastThirtyDayTimeCount[timeKey] ?: 0
                    lastThirtyDayTimeCount[timeKey] = timeCount + 1

                    val contentCount = lastThirtyDayContentCount[contentKey] ?: 0
                    lastThirtyDayContentCount[contentKey] = contentCount + 1

                    lastThirtyDayCount++
                }

                if (throwUp.date >= lastSevenDays) {
                    val timeCount = lastSevenDayTimeCount[timeKey] ?: 0
                    lastSevenDayTimeCount[timeKey] = timeCount + 1

                    val contentCount = lastSevenDayContentCount[contentKey] ?: 0
                    lastSevenDayContentCount[contentKey] = contentCount + 1

                    lastSevenDayCount++
                }
            }

            call.respond(Stats(
                    lastThirtyDayCount, lastThirtyDayTimeCount, lastThirtyDayContentCount,
                    lastSevenDayCount, lastSevenDayTimeCount, lastSevenDayContentCount
            ))
        }
    }
}
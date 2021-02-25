package com.joshafeinberg.oreotracker.server.stats

import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import com.joshafeinberg.oreotracker.sharedmodule.StatsService
import java.util.concurrent.TimeUnit
import kotlin.collections.set
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class StatsController(kodein: Kodein) : KodeinController(kodein), StatsService {

    private val repository: StatsRepository by instance()

    override suspend fun getStats(): Stats {
        val list = repository.list()

        val lastThirtyDays = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        val lastSevenDays = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)

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

        return Stats(
            lastThirtyDayCount, lastThirtyDayTimeCount, lastThirtyDayContentCount,
            lastSevenDayCount, lastSevenDayTimeCount, lastSevenDayContentCount
        )
    }
}

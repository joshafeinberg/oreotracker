package com.joshafeinberg.oreotracker.server

import com.joshafeinberg.oreotracker.sharedmodule.*

class Services(
        sicknessService: SicknessService,
        weightService: WeightService,
        statsService: StatsService
) : OreoTrackerNetwork,
        SicknessService by sicknessService,
        WeightService by weightService,
        StatsService by statsService {

    /*override suspend fun getThrowUps(): List<ThrowUp> = sicknessService.getThrowUps()

    override suspend fun postThrowUp(body: ThrowUp) = sicknessService.postThrowUp(body)

    override suspend fun getWeights(): List<Weight> = weightService.getWeights()

    override suspend fun postWeight(body: Weight) = weightService.postWeight(body)

    override suspend fun getStats(): Stats = statsService.getStats()*/
}
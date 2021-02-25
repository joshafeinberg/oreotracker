package com.joshafeinberg.oreotracker.server

import com.joshafeinberg.oreotracker.sharedmodule.OreoTrackerNetwork
import com.joshafeinberg.oreotracker.sharedmodule.SicknessService
import com.joshafeinberg.oreotracker.sharedmodule.StatsService
import com.joshafeinberg.oreotracker.sharedmodule.WeightService

class Services(
    sicknessService: SicknessService,
    weightService: WeightService,
    statsService: StatsService
) : OreoTrackerNetwork,
    SicknessService by sicknessService,
    WeightService by weightService,
    StatsService by statsService

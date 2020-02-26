package com.joshafeinberg.oreotracker.server.weight

import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import com.joshafeinberg.oreotracker.sharedmodule.WeightService
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class WeightController(kodein: Kodein) : KodeinController(kodein), WeightService {

    private val repository: WeightRepository by instance()

    override suspend fun getWeights(): List<Weight> {
        return repository.list()
    }

    override suspend fun postWeight(body: Weight) {
        repository.addWeight(body)
    }
}
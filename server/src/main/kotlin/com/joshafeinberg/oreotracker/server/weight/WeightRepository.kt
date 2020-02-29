package com.joshafeinberg.oreotracker.server.weight

import com.googlecode.objectify.Objectify
import com.joshafeinberg.oreotracker.sharedmodule.Weight

class WeightRepository(private val objectify: Objectify) {

    fun list(): List<Weight> = objectify.load().type(Weight::class.java).orderKey(true).list()

    fun addWeight(weight: Weight) {
        objectify.save().entity(weight)
    }
}

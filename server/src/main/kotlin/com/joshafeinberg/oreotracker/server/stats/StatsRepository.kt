package com.joshafeinberg.oreotracker.server.stats

import com.googlecode.objectify.Objectify
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp

class StatsRepository(private val objectify: Objectify) {

    fun list(): List<ThrowUp> = objectify.load().type(ThrowUp::class.java).orderKey(true).list()

}

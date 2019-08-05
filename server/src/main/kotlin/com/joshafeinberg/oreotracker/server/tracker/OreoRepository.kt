package com.joshafeinberg.oreotracker.server.tracker

import com.googlecode.objectify.Objectify
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp

class OreoRepository(private val objectify: Objectify) {

    fun list() = objectify.load().type(ThrowUp::class.java).orderKey(true).list()

    fun addThrowUp(throwUp: ThrowUp) {
        objectify.save().entity(throwUp)
    }

}
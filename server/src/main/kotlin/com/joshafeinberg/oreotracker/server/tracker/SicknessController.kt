package com.joshafeinberg.oreotracker.server.tracker

import com.joshafeinberg.oreotracker.server.di.KodeinController
import com.joshafeinberg.oreotracker.sharedmodule.SicknessService
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class SicknessController(kodein: Kodein) : KodeinController(kodein), SicknessService {
    private val repository: SicknessRepository by instance()

    override suspend fun getThrowUps(): List<ThrowUp> {
        println("Tried it")
        return repository.list()
    }

    override suspend fun postThrowUp(body: ThrowUp) {
        repository.addThrowUp(body)
    }
}
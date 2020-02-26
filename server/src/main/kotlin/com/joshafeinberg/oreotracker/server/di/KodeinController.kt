package com.joshafeinberg.oreotracker.server.di

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class KodeinController(override val kodein: Kodein) : KodeinAware
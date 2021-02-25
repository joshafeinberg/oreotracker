package com.joshafeinberg.oreotracker.server.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * Shortcut for binding singletons to the same type.
 */
inline fun <reified T : Any> Kodein.MainBuilder.bindSingleton(crossinline callback: (Kodein) -> T) {
    bind<T>() with singleton { callback(this@singleton.kodein) }
}

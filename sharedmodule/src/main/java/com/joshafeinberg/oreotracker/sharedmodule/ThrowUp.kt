package com.joshafeinberg.oreotracker.sharedmodule

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.io.Serializable
import kotlin.random.Random

@Entity
class ThrowUp : Serializable {
    @Id var date: Long = 0L

    lateinit var time: Time

    lateinit var content: Content

    constructor()

    constructor(date: Long,
                 time: Time,
                 content: Content) : this() {
        this.date = date
        this.time = time
        this.content = content
    }

    fun generateRandomItem(): ThrowUp {
        val date = System.currentTimeMillis()
        val time: Time = when (Random.nextInt(0, 4)) {
            0 -> Time.Overnight
            1 -> Time.AfterDinner
            2 -> Time.BetweenMeals
            else -> Time.ExactTime(System.nanoTime())
        }
        val content: Content = Content.values().random()
        return ThrowUp(date, time, content)
    }

    override fun toString(): String {
        return "ThrowUp(date=$date, time=$time, content=$content)"
    }

}
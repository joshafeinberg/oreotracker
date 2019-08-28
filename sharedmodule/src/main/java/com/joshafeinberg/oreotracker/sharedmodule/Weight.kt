package com.joshafeinberg.oreotracker.sharedmodule

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.io.Serializable

@Entity
class Weight : Serializable {
    @Id
    var date: Long = 0L

    var weight: Float = 0.0f

    constructor()

    constructor(date: Long,
                weight: Float) : this() {
        this.date = date
        this.weight = weight
    }

    override fun toString(): String {
        return "Weight(date=$date, weight=$weight)"
    }

}
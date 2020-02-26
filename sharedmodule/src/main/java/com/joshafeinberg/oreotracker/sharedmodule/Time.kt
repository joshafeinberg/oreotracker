package com.joshafeinberg.oreotracker.sharedmodule

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.googlecode.objectify.annotation.Subclass
import java.io.Serializable

@Subclass
sealed class Time : Serializable {
    @Subclass
    object Overnight : Time()

    @Subclass
    object BetweenMeals : Time()

    @Subclass
    object AfterDinner : Time()

    @Subclass
    class ExactTime() : Time() {
        var exactTime: Long = 0L

        constructor(exactTime: Long) : this() {
            this.exactTime = exactTime
        }

        override fun toString(): String {
            return "ExactTime(exactTime=$exactTime)"
        }
    }


    class TimeAdapter : TypeAdapter<Time>() {
        override fun write(writer: JsonWriter, value: Time?) {
            value?.let {
                writer.beginObject()
                writer.name("type")
                writer.value(value.javaClass.simpleName)
                if (it is ExactTime) {
                    writer.name("time")
                    writer.value(it.exactTime)
                }
                writer.endObject()
            }
        }

        override fun read(reader: JsonReader): Time {
            reader.beginObject()

            val token = reader.peek()
            var fieldName: String? = null

            if (token == JsonToken.NAME) {
                fieldName = reader.nextName()
            }

            var result: Time? = null

            if ("type" == fieldName) {
                reader.peek()
                result = when (val type = reader.nextString()) {
                    "Overnight" -> Overnight
                    "BetweenMeals" -> BetweenMeals
                    "AfterDinner" -> AfterDinner
                    "ExactTime" -> {
                        reader.nextName()
                        reader.peek()
                        ExactTime(reader.nextLong())
                    }
                    else -> throw IllegalStateException("Unknown type: $type")
                }
            }
            reader.endObject()
            return result!!
        }

    }
}
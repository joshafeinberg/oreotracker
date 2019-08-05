package com.joshafeinberg.oreotracker.sharedmodule

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.googlecode.objectify.annotation.Subclass
import java.io.Serializable

@Subclass
enum class Content : Serializable {
    GRANULAR, // some digestion
    CHUNKY, // food visible
    LIQUID; // bile

    companion object {
        fun fromString(value: String): Content {
            return enumValueOf(value.toUpperCase())
        }
    }

    class ContentAdapter : TypeAdapter<Content>() {
        override fun write(writer: JsonWriter, value: Content?) {
            writer.value(value?.name?.toLowerCase())
        }

        override fun read(reader: JsonReader): Content {
            return Content.fromString(reader.nextString())
        }

    }
}
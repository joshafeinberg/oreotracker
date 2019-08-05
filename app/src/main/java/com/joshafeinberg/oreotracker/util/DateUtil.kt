package com.joshafeinberg.oreotracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    const val DATE_FORMAT = "MM-dd-yyyy"
    const val TIME_FORMAT = "h:mm a"
}

fun Long.toFormattedDate(format: String): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(Date(this))
}
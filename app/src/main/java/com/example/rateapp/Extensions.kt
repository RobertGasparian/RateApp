package com.example.rateapp

import android.widget.TextView
import com.example.rateapp.datasource.domainmodel.WorkHour


fun String.extractIntRange(separator: String = "-"): IntRange? {
    val ints = this.split(separator)
    return if (ints.size == 2) {
        try {
            val from = ints[0].toInt()
            val to = ints[1].toInt()
            IntRange(from, to)
        } catch (ex: NumberFormatException) {
            null
        }
    } else null
}

fun String.getHours(separator: String = "-"): Pair<String, String>? {
    val hours = this.split(separator)
    return if (hours.size == 2) {
        Pair(hours[0], hours[1])
    } else null
}

val <T> T.exhaustive: T
    get() = this

fun TextView.setEndDrawable(id: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, id, 0)
}

fun List<WorkHour>.extractWorkHoursString(): String {
    val builder = StringBuilder()
    for((i, wh) in this.withIndex()) {
        builder.append(wh.toString())
        if (i != this.size - 1 ) {
            builder.append("\n")
        }
    }
    return builder.toString()
}

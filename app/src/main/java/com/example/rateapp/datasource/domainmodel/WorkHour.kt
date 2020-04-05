package com.example.rateapp.datasource.domainmodel


data class WorkHour(
    val days: IntRange,
    val from: String,
    val to: String
) {
    override fun toString(): String {
        return "${days.first} - ${days.last} | $from - $to"
    }
}
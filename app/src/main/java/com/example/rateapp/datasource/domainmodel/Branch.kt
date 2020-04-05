package com.example.rateapp.datasource.domainmodel

import android.location.Location


data class Branch(
    val head: Int,
    val title: String,
    val address: String,
    val location: Location,
    val contacts: String,
    val workHours: List<WorkHour>
)
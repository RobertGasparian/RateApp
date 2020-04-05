package com.example.rateapp.datasource.domainmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rate(
    val buy: Double,
    val sell: Double
): Parcelable
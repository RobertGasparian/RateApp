package com.example.rateapp.datasource.domainmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    val type: TransactionType,
    val rate: Rate
) : Parcelable

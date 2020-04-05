package com.example.rateapp.datasource.domainmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    val name: CurrencyType,
    val cashTrans: Transaction?,
    val nonCashTrans: Transaction?
): Parcelable

@Parcelize
enum class CurrencyType: Parcelable {
    USD,
    EUR,
    RUR,
    GBP,
    GEL,
    CAD,
    JPY,
    CHF,
    XAU,
    AUD;

    companion object {
        fun valueOrNull(name: String): CurrencyType? {
            return try {
                valueOf(name)
            } catch (ex: IllegalArgumentException) {
                null
            }
        }
    }
}
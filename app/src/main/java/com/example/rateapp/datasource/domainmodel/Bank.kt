package com.example.rateapp.datasource.domainmodel

import java.util.*

open class Bank(
    val id: String,
    val title: String,
    val date: Date,
    val logo: String?,
    val currencies: Map<CurrencyType, Currency>
)
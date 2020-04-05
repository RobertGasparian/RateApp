package com.example.rateapp.network.dto


data class CurrencyDTO(
    var name: String?,
    var cash: RateDTO?,
    var nonCash: RateDTO?
)
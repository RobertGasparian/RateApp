package com.example.rateapp.network.dto

data class BankDTO(
    var id: String,
    var title: String?,
    var date: Long?,
    var logo: String?,
    var currencies: List<CurrencyDTO>?
)
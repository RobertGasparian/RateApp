package com.example.rateapp.network.response

import com.example.rateapp.network.dto.BankDTO

data class BankResponse(
    var banks: List<BankDTO>
)
package com.example.rateapp.network.response

import com.example.rateapp.network.dto.*


data class BranchResponse (
    var date: Long?,
    var branches: List<BranchDTO>
)
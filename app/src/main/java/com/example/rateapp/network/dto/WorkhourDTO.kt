package com.example.rateapp.network.dto

import com.google.gson.annotations.SerializedName


data class WorkhourDTO(
    @SerializedName("days") var days: String?,
    @SerializedName("hours") var hours: String?
)
package com.example.rateapp.network.dto

import com.google.gson.annotations.SerializedName


data class LocationDTO(
    @SerializedName("lat") var lat: Double?,
    @SerializedName("lng") var lng: Double?
)
package com.example.rateapp.network.dto

import com.google.gson.annotations.SerializedName


data class TitleDTO(
    @SerializedName("en") var en: String?,
    @SerializedName("am") var am: String?,
    @SerializedName("ru") var ru: String?
)
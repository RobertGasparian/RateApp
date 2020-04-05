package com.example.rateapp.network

import com.example.rateapp.BuildConfig
import com.example.rateapp.network.deserializer.BankResponseDeserializer
import com.example.rateapp.network.deserializer.BranchDeserializer
import com.example.rateapp.network.response.BankResponse
import com.example.rateapp.network.response.BranchResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val BASE_URL = "http://rate.am/ws/mobile/v2/"
    }

    @GET("branches.ashx")
    fun getBranches(@Query("id") id: String): Call<BranchResponse>

    @GET("rates.ashx")
    fun getBanks(@Query("lang") lang: String = "en"): Call<BankResponse>
}
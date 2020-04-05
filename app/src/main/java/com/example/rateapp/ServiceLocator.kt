package com.example.rateapp

import com.example.rateapp.datasource.Repo
import com.example.rateapp.datasource.RepoImpl
import com.example.rateapp.network.ApiService
import com.example.rateapp.network.deserializer.BankResponseDeserializer
import com.example.rateapp.network.deserializer.BranchDeserializer
import com.example.rateapp.network.response.BankResponse
import com.example.rateapp.network.response.BranchResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//todo: implement DI system
object ServiceLocator {
    val repo: Repo = RepoImpl
    val apiService = createApiService()


    private fun getRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<BankResponse>() {}.type,
                BankResponseDeserializer()
            )
            .registerTypeAdapter(
                object : TypeToken<BranchResponse>() {}.type,
                BranchDeserializer()
            )
            .serializeNulls()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create()
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createApiService(client: OkHttpClient? = null): ApiService {
        return getRetrofit(client ?: OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build())
            .create(ApiService::class.java)
    }
}
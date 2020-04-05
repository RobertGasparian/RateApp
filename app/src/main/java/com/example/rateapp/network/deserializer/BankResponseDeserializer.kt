package com.example.rateapp.network.deserializer

import com.example.rateapp.datasource.domainmodel.CurrencyType
import com.example.rateapp.network.dto.BankDTO
import com.example.rateapp.network.dto.CurrencyDTO
import com.example.rateapp.network.dto.RateDTO
import com.example.rateapp.network.response.BankResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class BankResponseDeserializer : JsonDeserializer<BankResponse> {

    companion object {
        private const val TITLE_KEY = "title"
        private const val DATE_KEY = "date"
        private const val LOGO_KEY = "logo"
        private const val LIST_KEY = "list"
        private const val CASH_TYPE_KEY = "0"
        private const val NON_CASH_TYPE_KEY = "1"
        private const val BUY_KEY = "buy"
        private const val SELL_KEY = "sell"
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BankResponse {
        val list = mutableListOf<BankDTO>()
        val jsonObj = json?.asJsonObject
        jsonObj?.keySet()?.forEach { id ->
            val title = jsonObj[id].asJsonObject[TITLE_KEY].asString
            val date = jsonObj[id].asJsonObject[DATE_KEY].asLong
            val logo = jsonObj[id].asJsonObject[LOGO_KEY].asString
            val currencies = mutableListOf<CurrencyDTO>()
            val currenciesJson = jsonObj[id].asJsonObject[LIST_KEY].asJsonObject
            currenciesJson.keySet().forEach { currency ->

                val currencyJson = currenciesJson[currency].asJsonObject
                val cashJson = currencyJson[CASH_TYPE_KEY]?.asJsonObject
                val nonCashJson = currencyJson[NON_CASH_TYPE_KEY]?.asJsonObject
                val cashRate = cashJson?.let {
                    return@let RateDTO(buy = cashJson[BUY_KEY].asDouble, sell = cashJson[SELL_KEY].asDouble)
                }
                val nonCashRate = nonCashJson?.let {
                    return@let RateDTO(buy = nonCashJson[BUY_KEY].asDouble, sell = nonCashJson[SELL_KEY].asDouble)
                }
                currencies += CurrencyDTO(
                    name = currency,
                    cash = cashRate,
                    nonCash = nonCashRate
                )
            }
            list += BankDTO(
                id = id,
                title = title,
                date = date,
                logo = logo,
                currencies = currencies
            )
        }
        return BankResponse(list.toList())
    }

}
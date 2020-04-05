package com.example.rateapp.network.deserializer

import com.example.rateapp.network.dto.*
import com.example.rateapp.network.response.BranchResponse
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class BranchDeserializer : JsonDeserializer<BranchResponse> {

    companion object {
        private const val DATE_KEY = "date"
        private const val LIST_KEY = "list"
        private const val HEAD_KEY = "head"
        private const val TITLE_KEY = "title"
        private const val ADDRESS_KEY = "address"
        private const val LOCATION_KEY = "location"
        private const val CONTACTS_KEY = "contacts"
        private const val WORKHOURS_KEY = "workhours"
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BranchResponse {
        val list = mutableListOf<BranchDTO>()
        val jsonObj = json?.asJsonObject
        var date: Long? = null
        jsonObj?.let {
            date = it[DATE_KEY]?.asLong
            val jsonBranchList = it[LIST_KEY]?.asJsonObject
            jsonBranchList?.keySet()?.forEach { id ->
                jsonBranchList[id]?.asJsonObject?.let { branch ->
                    val head = branch[HEAD_KEY].asInt
                    val title = Gson().fromJson(
                        branch[TITLE_KEY]?.asJsonObject,
                        TitleDTO::class.java
                    )
                    val address = Gson().fromJson(
                        branch[ADDRESS_KEY]?.asJsonObject,
                        AddressDTO::class.java
                    )
                    val location = Gson().fromJson(
                        branch[LOCATION_KEY]?.asJsonObject,
                        LocationDTO::class.java
                    )
                    val contacts = branch[CONTACTS_KEY]?.asString

                    val workHours = Gson().fromJson(
                        branch[WORKHOURS_KEY]?.asJsonArray,
                        Array<WorkhourDTO>::class.java
                    ).toList()
                    list += BranchDTO(
                        id = id,
                        head = head,
                        title = title,
                        address = address,
                        location = location,
                        contacts = contacts,
                        workhours = workHours
                    )
                }
            }
        }
        return BranchResponse(date = date, branches = list.toList())
    }

}
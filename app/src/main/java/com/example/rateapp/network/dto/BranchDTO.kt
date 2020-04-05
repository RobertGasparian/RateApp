package com.example.rateapp.network.dto

import com.google.gson.annotations.SerializedName

class BranchDTO(
    var id: String,
    var head: Int?,
    var title: TitleDTO?,
    var address: AddressDTO?,
    var location: LocationDTO?,
    var contacts: String?,
    var workhours: List<WorkhourDTO>?
) {
    operator fun component1() = id
    operator fun component2() = head
    operator fun component3() = title?.en
    operator fun component4() = address?.en
    operator fun component5() = location
    operator fun component6() = contacts
    operator fun component7() = workhours

}
package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val districtDelimited = "district:"
        val stateDelimiter = "state:"

        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
                .substringBefore("/")
        val district = ocdDivisionId.substringAfter(districtDelimited,"")
                .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter,"")
                .substringBefore("/")

        return Division(ocdDivisionId, country, district, state)
    }

    @ToJson
    fun divisionToJson (division: Division): String {
        return division.id
    }
}
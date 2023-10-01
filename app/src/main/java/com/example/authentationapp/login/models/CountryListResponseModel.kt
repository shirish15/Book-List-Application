package com.example.authentationapp.login.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryListResponseModel(
    val data: CountryModelData?
)

@JsonClass(generateAdapter = true)
data class CountryModelData(
    val DZ: CountryModel?,
    val AO: CountryModel?,
    val BJ: CountryModel?,
    val BW: CountryModel?,
    val CF: CountryModel?
)


@JsonClass(generateAdapter = true)
data class CountryModel(
    val country: String?,
    val region: String?
)


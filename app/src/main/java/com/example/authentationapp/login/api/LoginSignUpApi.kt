package com.example.authentationapp.login.api

import com.example.authentationapp.login.models.CountryListResponseModel
import retrofit2.http.GET

interface LoginSignUpApi {
    @GET("data/v1/countries")
    suspend fun getCountriesList(): CountryListResponseModel
}

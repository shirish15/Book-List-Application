package com.example.authentationapp.login.repo

import com.example.authentationapp.login.models.CountryListResponseModel
import com.example.authentationapp.utils.ApiResultWrapper

interface ApiRepo {
    suspend fun getCountriesList(): ApiResultWrapper<CountryListResponseModel>
}
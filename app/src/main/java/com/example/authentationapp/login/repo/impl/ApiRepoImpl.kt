package com.example.authentationapp.login.repo.impl

import android.util.Log
import com.example.authentationapp.login.api.LoginSignUpApi
import com.example.authentationapp.login.models.CountryListResponseModel
import com.example.authentationapp.login.repo.ApiRepo
import com.example.authentationapp.utils.ApiResultWrapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ApiRepoImpl() : ApiRepo {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.first.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiService = retrofit.create(LoginSignUpApi::class.java)

    override suspend fun getCountriesList(): ApiResultWrapper<CountryListResponseModel> {
        return try {
            val response = apiService.getCountriesList()
            Log.e("API RESPONSE", "getCountriesList: $response")
            ApiResultWrapper.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResultWrapper.Error(e.toString())
        }
    }
}
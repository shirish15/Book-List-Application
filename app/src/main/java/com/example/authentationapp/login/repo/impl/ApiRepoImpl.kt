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
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRepoImpl @Inject constructor(retrofit: Retrofit) : ApiRepo {

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
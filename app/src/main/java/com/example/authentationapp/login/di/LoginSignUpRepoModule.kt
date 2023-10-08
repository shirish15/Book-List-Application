package com.example.authentationapp.login.di

import com.example.authentationapp.login.repo.ApiRepo
import com.example.authentationapp.login.repo.impl.ApiRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginSignUpRepoModule {

    @Binds
    @Singleton
    abstract fun apiRepo(apiRepo: ApiRepoImpl): ApiRepo
}
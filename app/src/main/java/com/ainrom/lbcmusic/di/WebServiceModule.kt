package com.ainrom.lbcmusic.di

import com.ainrom.lbcmusic.data.remote.WebService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class WebServiceModule {

    @Singleton
    @Provides
    fun provideWebService(): WebService {
        return Retrofit.Builder()
            .baseUrl("https://static.leboncoin.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }
}
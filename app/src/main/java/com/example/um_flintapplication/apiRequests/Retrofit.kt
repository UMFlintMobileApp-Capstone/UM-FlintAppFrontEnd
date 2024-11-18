package com.example.um_flintapplication.apiRequests

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    This is a generic Retrofit object that is just used to make calls easier
        Uses the ApiService Interface
    The BASE_URL may need to change for you
    10.0.2.2 is the default IP for android emulators (thanks google)
 */

object Retrofit {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
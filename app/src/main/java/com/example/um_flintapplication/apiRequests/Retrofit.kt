package com.example.um_flintapplication.apiRequests

import android.content.Context
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    This is a generic Retrofit object that is just used to make calls easier
        Uses the ApiService Interface
    The BASE_URL may need to change for you
    10.0.2.2 is the default IP for android emulators (thanks google)
 */
private const val BASE_URL = "http://10.0.2.2:8000/"

class Retrofit(context: Context) {
    val authClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context))
        .build()

    val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(authClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
                .build()
                .create(ApiService::class.java)
    }
}
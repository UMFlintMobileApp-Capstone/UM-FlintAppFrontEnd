package com.example.um_flintapplication.apiRequests

import android.content.Context
import com.example.um_flintapplication.Auth
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    val auth = Auth(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val httpBuilder = chain.request().url.newBuilder()

        // If token has been saved, add it to the request
        auth.silentLogin{ cred ->
            if(cred!=null)
                httpBuilder.addQueryParameter("token",cred.idToken)
            requestBuilder.url(httpBuilder.build())
        }

        return chain.proceed(requestBuilder.build())
    }
}
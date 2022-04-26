package com.example.fintracker_app.data.api

import com.example.fintracker_app.baseRetrofitUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseRetrofitUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java);
    }
}
package com.example.fintracker_app.data.api

import com.example.fintracker_app.dto.SignInDto
import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.CurrencyModel
import com.example.fintracker_app.model.SignInResponseModel
import com.example.fintracker_app.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/signUp")
    suspend fun signUpUser(@Body signUpDto: SignUpDto): Response<UserModel>

    @POST("api/signIn")
    suspend fun signInUser(@Body signInDto: SignInDto): Response<SignInResponseModel>

    @GET("api/currency")
    suspend fun getAllCurrency(): Response<MutableList<CurrencyModel>>
}
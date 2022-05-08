package com.example.fintracker_app.data.api

import com.example.fintracker_app.dto.SignInDto
import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.CurrencyModel
import com.example.fintracker_app.model.SignInResponseModel
import com.example.fintracker_app.model.SnapshotModel
import com.example.fintracker_app.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/signUp")
    suspend fun signUpUser(@Body signUpDto: SignUpDto): Response<UserModel>

    @POST("api/signIn")
    suspend fun signInUser(@Body signInDto: SignInDto): Response<SignInResponseModel>

    @GET("api/currency")
    suspend fun getAllCurrency(): Response<MutableList<CurrencyModel>>

    @GET("api/snapshots")
    suspend fun getAllSnapshots(@Header("Authorization") token: String): Response<MutableList<SnapshotModel>>
}
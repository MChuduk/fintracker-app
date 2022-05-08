package com.example.fintracker_app.data.api

import com.example.fintracker_app.dto.SignInDto
import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/signUp")
    suspend fun signUpUser(@Body signUpDto: SignUpDto): Response<UserModel>

    @POST("api/signIn")
    suspend fun signInUser(@Body signInDto: SignInDto): Response<SignInResponseModel>

    @GET("api/currency")
    suspend fun getAllCurrency(): Response<MutableList<CurrencyModel>>

    @POST("api/snapshots")
    suspend fun createSnapshot(@Header("Authorization") token: String): Response<SnapshotModel>

    @GET("api/snapshots")
    suspend fun getAllSnapshots(@Header("Authorization") token: String): Response<MutableList<SnapshotModel>>

    @DELETE("api/snapshots/{id}")
    suspend fun deleteSnapshot(@Header("Authorization") token: String, @Path("id") id: Int): Response<SnapshotModel>

    @GET("api/wallets?")
    suspend fun getAllWallets(@Header("Authorization") token: String, @Query("snapshot_id") snapshotId: Int): MutableList<WalletModel>

    @POST("api/wallets")
    suspend fun createWallet(@Header("Authorization") token: String, @Body model: WalletModel): Response<WalletModel>
}
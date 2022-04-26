package com.example.fintracker_app.data.api

import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.ResponseModel
import com.example.fintracker_app.model.UserModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/signUp")
    suspend fun signUpUser(@Body signUpDto: SignUpDto): Response<UserModel>
}
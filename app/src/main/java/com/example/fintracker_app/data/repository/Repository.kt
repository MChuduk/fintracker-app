package com.example.fintracker_app.data.repository

import com.example.fintracker_app.data.api.RetrofitInstance
import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.ResponseModel
import com.example.fintracker_app.model.UserModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class Repository {
    suspend fun signUpUser(email: String, password: String): Response<UserModel> {
        val signUpDto = SignUpDto(email, password);
        return RetrofitInstance.api.signUpUser(signUpDto);
    }
}
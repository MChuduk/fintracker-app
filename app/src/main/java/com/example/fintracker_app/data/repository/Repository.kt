package com.example.fintracker_app.data.repository

import com.example.fintracker_app.data.api.RetrofitInstance
import com.example.fintracker_app.dto.SignInDto
import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.CurrencyModel
import com.example.fintracker_app.model.SignInResponseModel
import com.example.fintracker_app.model.UserModel
import retrofit2.Response

class Repository {
    suspend fun signUpUser(email: String, password: String): Response<UserModel> {
        val signUpDto = SignUpDto(email, password);
        return RetrofitInstance.api.signUpUser(signUpDto);
    }

    suspend fun signInUser(email: String, password: String): Response<SignInResponseModel> {
        val signInDto = SignInDto(email, password);
        return RetrofitInstance.api.signInUser(signInDto);
    }

    suspend fun getAllCurrency(): Response<MutableList<CurrencyModel>> {
        return RetrofitInstance.api.getAllCurrency();
    }
}
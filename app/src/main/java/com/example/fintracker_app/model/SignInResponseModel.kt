package com.example.fintracker_app.model

data class SignInResponseModel(
    val type: String,
    val id: Int,
    val token: String,
)
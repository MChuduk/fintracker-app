package com.example.fintracker_app.model

import java.io.Serializable

data class WalletModel (
    val row_id: Int,
    val name: String,
    val currency_id: Int,
    val user_id: Int,
): Serializable;
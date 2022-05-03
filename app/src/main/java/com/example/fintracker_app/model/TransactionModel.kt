package com.example.fintracker_app.model

import java.io.Serializable

data class TransactionModel (
    val row_id: Int,
    val type_id: Int,
    val note: String,
    val amount: Float,
    val date: String,
    val wallet_id: Int,
    val category_id: Int,
    val repeat: Int,
) : Serializable;
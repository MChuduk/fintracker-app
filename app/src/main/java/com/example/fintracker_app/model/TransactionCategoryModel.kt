package com.example.fintracker_app.model

import java.io.Serializable

data class TransactionCategoryModel (
    val row_id: Int,
    val name: String,
    val user_id: Int,
) : Serializable;
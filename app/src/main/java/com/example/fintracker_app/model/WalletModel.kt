package com.example.fintracker_app.model

data class WalletModel (
    override val id: Int,
    val name: String,
    val currency_id: Int,
    val user_id: Int,
): BaseDataModel(id);
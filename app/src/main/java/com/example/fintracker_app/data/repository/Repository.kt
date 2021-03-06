package com.example.fintracker_app.data.repository

import com.example.fintracker_app.data.api.RetrofitInstance
import com.example.fintracker_app.dto.SignInDto
import com.example.fintracker_app.dto.SignUpDto
import com.example.fintracker_app.model.*
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

    suspend fun createSnapshot(token: String): Response<SnapshotModel> {
        return RetrofitInstance.api.createSnapshot(token);
    }

    suspend fun getAllSnapshots(token: String): Response<MutableList<SnapshotModel>> {
        return RetrofitInstance.api.getAllSnapshots(token);
    }

    suspend fun getOneSnapshot(token: String, id: Int): Response<SnapshotModel> {
        return RetrofitInstance.api.getOneSnapshot(token, id);
    }

    suspend fun deleteSnapshot(token: String, id: Int): Response<SnapshotModel> {
        return RetrofitInstance.api.deleteSnapshot(token, id);
    }

    suspend fun getAllWallets(token: String, snapshotId: Int): Response<MutableList<WalletModel>> {
        return RetrofitInstance.api.getAllWallets(token, snapshotId);
    }

    suspend fun createWallet(token: String, model: WalletModel): Response<WalletModel> {
        return RetrofitInstance.api.createWallet(token, model);
    }

    suspend fun createTransactionCategory(token: String, model: TransactionCategoryModel): Response<TransactionCategoryModel> {
        return RetrofitInstance.api.createTransactionCategory(token, model);
    }

    suspend fun getAllTransactionCategories(token: String, snapshotId: Int): Response<MutableList<TransactionCategoryModel>> {
        return RetrofitInstance.api.getAllTransactionCategories(token, snapshotId);
    }

    suspend fun createTransaction(token: String, model: TransactionModel): Response<TransactionModel> {
        return RetrofitInstance.api.createTransaction(token, model);
    }

    suspend fun getAllTransactions(token: String, snapshotId: Int): Response<MutableList<TransactionModel>> {
        return RetrofitInstance.api.getAllTransactions(token, snapshotId);
    }
}
package com.example.fintracker_app.services

import android.content.Context
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.ErrorModel
import com.example.fintracker_app.model.SnapshotModel
import com.google.gson.Gson

class SnapshotsService(val context: Context) {

    suspend fun getAll(token: String): MutableList<SnapshotModel> {
        var snapshotsList: MutableList<SnapshotModel> = mutableListOf();
        try {
            val repo = Repository();
            val response = repo.getAllSnapshots("Bearer $token");
            val snapshots = response.body();
            if(snapshots === null) {
                val err = Gson().fromJson(
                    response.errorBody()!!.charStream(),
                    ErrorModel::class.java
                )
                throw Exception(err.message);
            }
            snapshotsList = snapshots;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        }
        return snapshotsList;
    }
}
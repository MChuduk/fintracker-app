package com.example.fintracker_app.services

import android.content.Context
import android.content.SharedPreferences
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.ErrorModel
import com.example.fintracker_app.model.SnapshotModel
import com.google.gson.Gson

class SnapshotsService(val context: Context) {

    suspend fun create(token: String): SnapshotModel? {
        try {
            val repo = Repository();
            val response = repo.createSnapshot("Bearer $token");
            val snapshot = response.body();
            if(snapshot === null) {
                val err = Gson().fromJson(
                    response.errorBody()!!.charStream(),
                    ErrorModel::class.java
                )
                throw Exception(err.message);
            }
            return snapshot;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            return null;
        }
    }

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

    suspend fun getOne(token: String, id: Int): SnapshotModel? {
        try {
            val snapshots = getAll(token);
            for(snapshot in snapshots) {
                if(snapshot.id == id) {
                    return snapshot;
                }
            }
            return null;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            return null;
        }
    }

    suspend fun getLatest(token: String): SnapshotModel? {
        return try {
            val snapshots = getAll(token);
            if(snapshots.count() > 0) snapshots[0] else null;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        }
    }

    suspend fun deleteSnapshot(token: String, id: Int): SnapshotModel? {
        try {
            val repo = Repository();
            val response = repo.deleteSnapshot("Bearer $token", id);
            val snapshot = response.body();
            if(snapshot === null) {
                val err = Gson().fromJson(
                    response.errorBody()!!.charStream(),
                    ErrorModel::class.java
                )
                throw Exception(err.message);
            }
            return snapshot;
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            return null;
        }
    }
}
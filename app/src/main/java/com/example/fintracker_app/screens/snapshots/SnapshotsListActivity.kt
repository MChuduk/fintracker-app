package com.example.fintracker_app.screens.snapshots

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.fintracker_app.R
import com.example.fintracker_app.adapters.SnapshotsAdapter
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.model.SnapshotModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.screens.transactions.TransactionsUpsertActivity
import com.example.fintracker_app.services.SnapshotsService
import com.example.fintracker_app.services.WalletsService
import com.example.fintracker_app.services.showMessage
import kotlinx.coroutines.launch

class SnapshotsListActivity : ModelListActivity<SnapshotModel>() {

    private lateinit var preferences: SharedPreferences;

    private lateinit var walletsService: WalletsService;
    private lateinit var service: SnapshotsService;

    override fun onCreate(savedInstanceState: Bundle?) {
        initServices();
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE;
        lifecycleScope.launch {
            val token = preferences.getString("UserToken", "Undefined");
            itemList = service.getAll(token!!);
            recyclerView.adapter = SnapshotsAdapter(itemList);
            progressBar.visibility = View.GONE;
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.snapshots_list_action_bar_menu, menu);
        return true;
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.DeleteItem)!!.isEnabled = (getSelectedItems().count() >= 1);
        menu.findItem(R.id.DownloadItem)!!.isEnabled = (getSelectedItems().count() == 1);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.DownloadItem -> onDownloadItemSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onItemCreate() {
        super.onItemCreate();
        lifecycleScope.launch {
            val token = preferences.getString("UserToken", "Undefined");
            val snapshot = service.create(token!!);
            itemList.add(snapshot!!);
            recyclerView.adapter = SnapshotsAdapter(itemList);

            walletsService.attachAllToSnapshot(token);
        }
    }

    override fun onItemDelete() {
        super.onItemDelete();
        lifecycleScope.launch {
            val token = preferences.getString("UserToken", "Undefined");
            val items = getSelectedItems();
            for(item in items) {
                service.deleteSnapshot(token!!, item.id);
            }
            itemList = service.getAll(token!!);
            recyclerView.adapter = SnapshotsAdapter(itemList);
        }
    }

    private fun onDownloadItemSelected() {
        lifecycleScope.launch {
            val token = preferences.getString("UserToken", "Undefined");
            val snapshot = getSelectedItems()[0];
            walletsService.applySnapshot(token!!, snapshot.id);
            showMessage(applicationContext, "Снапшот от ${snapshot.created_at} успешно загружен");
        }
    }

    private fun initServices() {
        walletsService = WalletsService(applicationContext);
        service = SnapshotsService(applicationContext);
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
    }
}
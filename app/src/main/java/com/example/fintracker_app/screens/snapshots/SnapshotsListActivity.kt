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
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.model.SnapshotModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.screens.transactions.TransactionsUpsertActivity
import com.example.fintracker_app.services.SnapshotsService
import kotlinx.coroutines.launch

class SnapshotsListActivity : ModelListActivity<SnapshotModel>() {

    private lateinit var preferences: SharedPreferences;
    private lateinit var service: SnapshotsService;

    override fun onCreate(savedInstanceState: Bundle?) {
        service = SnapshotsService(applicationContext);
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE;
        lifecycleScope.launch {
            val token = preferences.getString("UserToken", "Undefined");
            itemList = service.getAll(token!!);
            recyclerView.adapter = SnapshotsAdapter(applicationContext, itemList);
            progressBar.visibility = View.GONE;
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.snapshots_list_action_bar_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.CreateItem -> onItemCreate();
            R.id.DeleteItem -> onItemDelete();
            R.id.BackItem -> onItemBack();
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.DeleteItem)!!.isEnabled = (getSelectedItems().count() >= 1);
        return true;
    }

    override fun onItemCreate() {
        super.onItemCreate();
    }
}
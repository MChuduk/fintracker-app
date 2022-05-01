package com.example.fintracker_app.screens.wallets

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.services.WalletsService
import com.example.fintracker_app.services.base.CrudService

class WalletsListActivity : ModelListActivity<WalletModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        service = WalletsService(applicationContext);
        super.onCreate(savedInstanceState);
    }

    override fun onResume() {
        super.onResume()
        updateAdapter();
    }

    override fun onItemCreate() {
        super.onItemCreate();
        val intent = Intent(applicationContext, WalletUpsertActivity::class.java);
        intent.putExtra("UpsertMode", "Insert");
        startActivity(intent);
    }

    override fun onItemEdit() {
        super.onItemEdit();
        val selectedWallet = getSelectedItems()[0];
        val intent = Intent(applicationContext, WalletUpsertActivity::class.java);
        intent.putExtra("UpsertMode", "Update");
        intent.putExtra("SelectedWallet", selectedWallet);
        startActivity(intent);
    }

    override fun onItemDelete() {
        super.onItemDelete();
        updateAdapter();
    }

    private fun updateAdapter() {
        recyclerView.adapter = WalletsAdapter(itemList);
    }
}
package com.example.fintracker_app.screens.transactions

import android.content.Intent
import android.os.Bundle
import com.example.fintracker_app.adapters.TransactionsAdapter
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.screens.wallets.WalletUpsertActivity
import com.example.fintracker_app.services.TransactionsService
import com.example.fintracker_app.services.WalletsService

class TransactionsListActivity : ModelListActivity<TransactionModel>() {

    private lateinit var service: TransactionsService;

    override fun onCreate(savedInstanceState: Bundle?) {
        service = TransactionsService(applicationContext);
        super.onCreate(savedInstanceState);
    }

    override fun onResume() {
        super.onResume()
        itemList = service.getAll();
        recyclerView.adapter = TransactionsAdapter(itemList);
    }

    override fun onItemCreate() {
        super.onItemCreate();
        val intent = Intent(applicationContext, TransactionsUpsertActivity::class.java);
        intent.putExtra("UpsertMode", "Insert");
        startActivity(intent);
    }

    override fun onItemEdit() {
        super.onItemEdit();
        val selectedItem = getSelectedItems()[0];
        val intent = Intent(applicationContext, TransactionsUpsertActivity::class.java);
        intent.putExtra("UpsertMode", "Update");
        intent.putExtra("SelectedItem", selectedItem);
        startActivity(intent);
    }

    override fun onItemDelete() {
        super.onItemDelete();
        val items = getSelectedItems();
        for(item in items) {
            service.delete(item.row_id);
        }
        itemList = service.getAll();
        recyclerView.adapter = TransactionsAdapter(itemList);
    }
}
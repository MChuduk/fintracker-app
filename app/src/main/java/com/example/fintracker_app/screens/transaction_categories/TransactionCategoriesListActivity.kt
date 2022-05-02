package com.example.fintracker_app.screens.transaction_categories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fintracker_app.R
import com.example.fintracker_app.adapters.TransactionCategoryAdapter
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.model.TransactionCategoryModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.screens.wallets.WalletUpsertActivity
import com.example.fintracker_app.services.TransactionsService
import com.example.fintracker_app.services.WalletsService

class TransactionCategoriesListActivity : ModelListActivity<TransactionCategoryModel>() {

    private lateinit var service: TransactionsService;

    override fun onCreate(savedInstanceState: Bundle?) {
        service = TransactionsService(applicationContext);
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        itemList = service.getAllCategories();
        recyclerView.adapter = TransactionCategoryAdapter(itemList);
    }

    override fun onItemCreate() {
        super.onItemCreate();
        val intent = Intent(applicationContext, TransactionCategoriesUpsertActivity::class.java);
        intent.putExtra("UpsertMode", "Insert");
        startActivity(intent);
    }

    override fun onItemEdit() {
        super.onItemEdit();
        val selectedItem = getSelectedItems()[0];
        val intent = Intent(applicationContext, TransactionCategoriesUpsertActivity::class.java);
        intent.putExtra("UpsertMode", "Update");
        intent.putExtra("SelectedTransactionCategory", selectedItem);
        startActivity(intent);
    }

    override fun onItemDelete() {
        super.onItemDelete();
        val items = getSelectedItems();
        for(item in items) {
            service.deleteCategoryById(item.row_id);
        }
        itemList = service.getAllCategories();
        recyclerView.adapter = TransactionCategoryAdapter(itemList);
    }
}
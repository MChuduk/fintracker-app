package com.example.fintracker_app.screens.transaction_categories

import android.content.Intent
import android.os.Bundle
import com.example.fintracker_app.adapters.TransactionCategoryAdapter
import com.example.fintracker_app.model.TransactionCategoryModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.services.TransactionCategoriesService

class TransactionCategoriesListActivity : ModelListActivity<TransactionCategoryModel>() {

    private lateinit var service: TransactionCategoriesService;

    override fun onCreate(savedInstanceState: Bundle?) {
        service = TransactionCategoriesService(applicationContext);
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        itemList = service.getAll();
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
        recyclerView.adapter = TransactionCategoryAdapter(itemList);
    }
}
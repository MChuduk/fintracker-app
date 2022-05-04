package com.example.fintracker_app.screens.transactions

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.example.fintracker_app.R
import com.example.fintracker_app.adapters.TransactionsAdapter
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.dialogs.SortByPeriodDialog
import com.example.fintracker_app.model.TransactionModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.base.ModelListActivity
import com.example.fintracker_app.screens.wallets.WalletUpsertActivity
import com.example.fintracker_app.services.TransactionsService
import com.example.fintracker_app.services.WalletsService
import java.time.LocalDate

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.transactions_list_action_bar_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item);
        when(item.itemId) {
            R.id.StatsItem -> onStatsItemSelected();
            R.id.SortByPeriodItem -> onShowSortByPeriodDialog();
            R.id.ClearSortsItem -> onClearStatsItemSelected();
        };

        return super.onOptionsItemSelected(item);
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortByPeriod(dateStringFrom: String, dateStringTo: String) {
        val dateFrom = LocalDate.parse(dateStringFrom);
        val dateTo = LocalDate.parse(dateStringTo);

        val sortedItems: MutableList<TransactionModel> = arrayListOf();
        for(item in itemList) {
            val itemDate = LocalDate.parse(item.date);
            if(itemDate in dateFrom..dateTo)
            sortedItems.add(item);
        }
        itemList = sortedItems;
        recyclerView.adapter = TransactionsAdapter(itemList);
    }


    fun onStatsItemSelected() {

    }

    private fun onClearStatsItemSelected() {
        itemList = service.getAll();
        recyclerView.adapter = TransactionsAdapter(itemList);
    }

    private fun onShowSortByPeriodDialog() {
        val dialog = SortByPeriodDialog(this);
        dialog.show(supportFragmentManager, "SortByPeriod");
    }
}
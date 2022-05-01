package com.example.fintracker_app.screens

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fintracker_app.R
import com.example.fintracker_app.adapters.WalletsAdapter
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.WalletsService

class WalletsListActivity : AppCompatActivity() {

    private lateinit var recyclerViewWallets: RecyclerView;

    private lateinit var walletsList: MutableList<WalletModel>;

    private lateinit var walletsService: WalletsService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallets_list)
        findViews();
        initServices();
    }

    override fun onResume() {
        super.onResume()
        walletsList = walletsService.getAll();
        showWalletsList();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.wallets_action_bar_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.CreateWalletItem -> onCreateWalletItemSelected();
            R.id.DeleteWalletItem -> onDeleteWalletItemSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    private fun showWalletsList() {
        recyclerViewWallets.adapter = WalletsAdapter(walletsList);
        val linearLayoutManager = LinearLayoutManager(applicationContext);
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewWallets.layoutManager = linearLayoutManager
    }

    private fun onCreateWalletItemSelected() {
        val intent = Intent(applicationContext, CreateWalletActivity::class.java);
        startActivity(intent);
    }

    private fun onDeleteWalletItemSelected() {
        val notSelectedWallets: MutableList<WalletModel> = mutableListOf();
        for (index in walletsList.indices) {
            val view = recyclerViewWallets.layoutManager?.findViewByPosition(index);
            val isSelected = view?.findViewById<CheckBox>(R.id.checkBoxWalletSelected)?.isChecked;
            if(isSelected == false) {
                notSelectedWallets.add(walletsList[index]);
            } else {
                walletsService.deleteById(walletsList[index].id);
            }
        }
        walletsList = notSelectedWallets;
        showWalletsList();
    }

    private fun initServices() {
        walletsService = WalletsService(applicationContext);
    }

    private fun findViews() {
        recyclerViewWallets = findViewById(R.id.recyclerViewWallets);
    }
}
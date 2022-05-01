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
            R.id.CreateItem -> onCreateWalletItemSelected();
            R.id.DeleteItem -> onDeleteWalletItemSelected();
            R.id.EditItem -> onEditWalletItemSelected();
            R.id.BackItem -> onBackItemSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.EditItem)!!.isEnabled = (getSelectedItems().count() == 1);
        menu.findItem(R.id.DeleteItem)!!.isEnabled = (getSelectedItems().count() >= 1);
        return true;
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

    private fun onEditWalletItemSelected() {
        val selectedWallet = getSelectedItems()[0];
        val intent = Intent(applicationContext, EditWalletActivity::class.java);
        intent.putExtra("SelectedWallet", selectedWallet);
        startActivity(intent);
    }

    private fun onDeleteWalletItemSelected() {
        val items = getSelectedItems();
        for(item in items) {
            walletsService.deleteById(item.id);
        }
        walletsList = walletsService.getAll();
        showWalletsList();
    }

    private fun getSelectedItems(): MutableList<WalletModel> {
        val items: MutableList<WalletModel> = mutableListOf();
        for (index in walletsList.indices) {
            val view = recyclerViewWallets.layoutManager?.findViewByPosition(index);
            val isSelected = view?.findViewById<CheckBox>(R.id.checkBoxWalletSelected)?.isChecked;
            if(isSelected == true) {
                items.add(walletsList[index]);
            }
        }
        return items;
    }

    private fun onBackItemSelected() {
        val intent = Intent(applicationContext, MainActivity::class.java);
        startActivity(intent);
    }

    private fun initServices() {
        walletsService = WalletsService(applicationContext);
    }

    private fun findViews() {
        recyclerViewWallets = findViewById(R.id.recyclerViewWallets);
    }
}
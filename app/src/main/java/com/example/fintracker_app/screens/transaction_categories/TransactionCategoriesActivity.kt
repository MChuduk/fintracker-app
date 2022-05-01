package com.example.fintracker_app.screens.transaction_categories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.fintracker_app.R
import com.example.fintracker_app.screens.MainActivity

class TransactionCategoriesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_categories)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crud_action_bar_menu, menu);
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

    private fun onCreateWalletItemSelected() {

    }

    private fun onEditWalletItemSelected() {

    }

    private fun onDeleteWalletItemSelected() {

    }

    private fun onBackItemSelected() {
        val intent = Intent(applicationContext, MainActivity::class.java);
        startActivity(intent);
    }
}
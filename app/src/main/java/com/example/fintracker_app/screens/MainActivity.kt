package com.example.fintracker_app.screens

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.screens.snapshots.SnapshotsListActivity
import com.example.fintracker_app.screens.transaction_categories.TransactionCategoriesListActivity
import com.example.fintracker_app.screens.transactions.TransactionsListActivity

import com.example.fintracker_app.screens.wallets.WalletsListActivity
import com.example.fintracker_app.services.database.DatabaseHelper


class MainActivity : AppCompatActivity() {

    private lateinit var textViewUserProfile: TextView;

    private lateinit var dbHelper: DatabaseHelper;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initServices();
        findViews();
        setUserInfo();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.ExitItem -> showExitDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    fun onWalletButtonClick(view: View) {
        val intent = Intent(applicationContext, WalletsListActivity::class.java);
        startActivity(intent);
    }

    fun onTransactionCategoriesButtonClick(view: View) {
        val intent = Intent(applicationContext, TransactionCategoriesListActivity::class.java);
        startActivity(intent);
    }

    fun onTransactionsButtonClick(view: View) {
        val intent = Intent(applicationContext, TransactionsListActivity::class.java);
        startActivity(intent);
    }

    fun onSnapshotsButtonClick(view: View) {
        val intent = Intent(applicationContext, SnapshotsListActivity::class.java);
        startActivity(intent);
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("??????????")
            .setMessage("???? ?????????????????????????? ???????????? ???????????")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("????") { _, _ -> exit() }
            .setNegativeButton("??????", null)
            .show()
    }

    private fun exit() {
        val preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        val editor = preferences.edit();
        editor.remove("UserId")
        editor.remove("UserEmail");
        editor.remove("UserToken")
        editor.apply();

        dbHelper.clearTableWallets();
        dbHelper.clearTableTransactionCategories();
        dbHelper.clearTableTransactions();

        val intent = Intent(applicationContext, SignInActivity::class.java);
        startActivity(intent);
    }

    private fun setUserInfo() {
        val preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        val userEmail = preferences.getString("UserEmail", "Undefined");
        textViewUserProfile.text = "????????????????????????: $userEmail";
    }

    private fun initServices() {
        dbHelper = DatabaseHelper(applicationContext);
    }

    private fun findViews() {
        textViewUserProfile = findViewById(R.id.textViewUserProfile);
    }
}
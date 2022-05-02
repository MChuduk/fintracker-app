package com.example.fintracker_app.screens.transaction_categories

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.model.TransactionCategoryModel
import com.example.fintracker_app.model.TransactionTypeModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.screens.wallets.WalletsListActivity
import com.example.fintracker_app.services.TransactionsService
import com.example.fintracker_app.services.selectItemByValue

class TransactionCategoriesUpsertActivity : AppCompatActivity() {

    private lateinit var transactionCategoryName: EditText;
    private lateinit var buttonConfirm: Button;

    private lateinit var preferences: SharedPreferences;
    private lateinit var transactionsService: TransactionsService;
    private lateinit var selectedTransactionCategory: TransactionCategoryModel;
    private lateinit var upsertMode: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_categories_upsert)
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        findViews();
        initServices();
        setUpsertMode();
        setSelectedTransactionType();
    }

    fun onConfirmButtonClick(view: View) { confirm(); }

    private fun confirm() {
        val name = transactionCategoryName.text.toString();
        val userId = preferences.getInt("UserId", -1);

        var transactionCategory: TransactionCategoryModel? = null;
        if(upsertMode == "Insert") {
            transactionCategory = transactionsService.createCategory(null, name, userId);
        } else {
            transactionCategory = transactionsService.editCategory(selectedTransactionCategory.row_id, name, userId);
        }
        if(transactionCategory != null) {
            val intent = Intent(applicationContext,TransactionCategoriesListActivity::class.java);
            startActivity(intent);
        }
    }

    private fun setUpsertMode() {
        if(intent.hasExtra("UpsertMode")) {
            upsertMode = intent.getStringExtra("UpsertMode")!!;
            if(upsertMode == "Insert") {
                buttonConfirm.text = "Создать";
            } else {
                buttonConfirm.text = "Изменить";
            }
        }
    }
    private fun setSelectedTransactionType() {
        if(intent.hasExtra("SelectedTransactionCategory")) {
            selectedTransactionCategory = intent.getSerializableExtra("SelectedTransactionCategory") as TransactionCategoryModel;
            transactionCategoryName.setText(selectedTransactionCategory.name);
        }
    }

    private fun initServices() {
        transactionsService = TransactionsService(applicationContext);
    }

    private fun findViews() {
        transactionCategoryName = findViewById(R.id.editTextTransactionCategoryName);
        buttonConfirm = findViewById(R.id.buttonConfirm);
    }
}
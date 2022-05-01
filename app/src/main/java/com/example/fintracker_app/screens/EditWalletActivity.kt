package com.example.fintracker_app.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.CurrencyService
import com.example.fintracker_app.services.WalletsService
import com.example.fintracker_app.services.selectItemByValue

class EditWalletActivity : AppCompatActivity() {

    private lateinit var walletName: EditText;
    private lateinit var currencySpinner: Spinner;

    private lateinit var selectedWallet: WalletModel;

    private lateinit var preferences: SharedPreferences;
    private lateinit var currencyService: CurrencyService;
    private lateinit var walletsService: WalletsService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wallet)
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        findViews();
        initServices();
        setCurrencySpinnerValues();
        setSelectedWallet();
    }

    fun onEditButtonClick(view: View) { editWallet() }

    private fun editWallet() {
        val name = walletName.text.toString();
        val currencyId = currencyService.findByName(currencySpinner.selectedItem.toString())?.id;
        val userId = preferences.getInt("UserId", -1);

        val wallet = walletsService.edit(selectedWallet.id, name, currencyId!!, userId);
        if(wallet !== null) {
            val intent = Intent(applicationContext, WalletsListActivity::class.java);
            startActivity(intent);
        }
    }

    private fun setSelectedWallet() {
        if(intent.hasExtra("SelectedWallet")) {
            selectedWallet = intent.getSerializableExtra("SelectedWallet") as WalletModel;

            walletName.setText(selectedWallet.name);

            val currencyName = currencyService.findById(selectedWallet.currency_id)?.name;
            currencySpinner.selectItemByValue(currencyName!!);
        }
    }

    private fun setCurrencySpinnerValues() {
        val values = mutableListOf<String>();
        val currencyList = currencyService.getAll();
        for(currency in currencyList) {
            values.add(currency.name);
        }
        currencySpinner.adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, values);
    }

    private fun initServices() {
        currencyService = CurrencyService(applicationContext);
        walletsService = WalletsService(applicationContext);
    }

    private fun findViews() {
        walletName = findViewById(R.id.editTextWalletName);
        currencySpinner = findViewById(R.id.spinnerCurrency);
    }
}
package com.example.fintracker_app.screens.wallets

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.fintracker_app.R
import com.example.fintracker_app.appPreferencesName
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.CurrencyService
import com.example.fintracker_app.services.WalletsService
import com.example.fintracker_app.services.selectItemByValue

class WalletUpsertActivity : AppCompatActivity() {

    private lateinit var walletName: EditText;
    private lateinit var currencySpinner: Spinner;
    private lateinit var buttonConfirm: Button;

    private lateinit var preferences: SharedPreferences;
    private lateinit var currencyService: CurrencyService;
    private lateinit var walletsService: WalletsService;
    private lateinit var selectedWallet: WalletModel;
    private lateinit var upsertMode: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_upsert);
        preferences = getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE);
        findViews();
        initServices();
        setUpsertMode();
        setCurrencySpinnerValues();
        setSelectedWallet();
    }

    fun onConfirmButtonClick (view: View) { confirm(); }

    private fun confirm() {
        val name = walletName.text.toString();
        val currencyId = currencyService.findByName(currencySpinner.selectedItem.toString())?.id;
        val userId = preferences.getInt("UserId", -1);

        var wallet: WalletModel?;
        if(upsertMode == "Insert") {
            wallet = walletsService.create(null, name, currencyId!!, userId);
        } else {
            wallet = walletsService.edit(selectedWallet.row_id, name, currencyId!!, userId);
        }
        if(wallet != null) {
            val intent = Intent(applicationContext, WalletsListActivity::class.java);
            startActivity(intent);
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

    private fun setUpsertMode() {
        if(intent.hasExtra("UpsertMode")) {
            upsertMode = intent.getStringExtra("UpsertMode")!!;
            if(upsertMode == "Insert") {
                buttonConfirm.text = "??????????????";
            } else {
                buttonConfirm.text = "????????????????";
            }
        }
    }

    private fun setSelectedWallet() {
        if(intent.hasExtra("SelectedItem")) {
            selectedWallet = intent.getSerializableExtra("SelectedItem") as WalletModel;

            walletName.setText(selectedWallet.name);

            val currencyName = currencyService.findById(selectedWallet.currency_id)?.name;
            currencySpinner.selectItemByValue(currencyName!!);
        }
    }

    private fun findViews() {
        walletName = findViewById(R.id.editTextWalletName);
        currencySpinner = findViewById(R.id.spinnerCurrency);
        buttonConfirm = findViewById(R.id.buttonConfirm);
    }
}
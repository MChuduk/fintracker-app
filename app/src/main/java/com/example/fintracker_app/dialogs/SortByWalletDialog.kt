package com.example.fintracker_app.dialogs

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.fintracker_app.R
import com.example.fintracker_app.components.DatePicker
import com.example.fintracker_app.screens.transactions.TransactionsListActivity
import com.example.fintracker_app.services.WalletsService
import java.util.*

class SortByWalletDialog(private val activity: TransactionsListActivity): AppCompatDialogFragment() {

    private lateinit var spinnerWallet: Spinner;
    private lateinit var buttonSort: Button;

    private lateinit var walletsService: WalletsService;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity);
        val view = layoutInflater.inflate(R.layout.dialog_sort_by_wallet, null);
        initServices();
        findViews(view);
        setWalletSpinnerValues();

        buttonSort.setOnClickListener {
            val walletId = walletsService.findByName(spinnerWallet.selectedItem.toString())?.row_id;
            activity.sortByWallet(walletId!!);
            dismiss();
        }

        builder.setView(view);
        return builder.create()
    }

    private fun setWalletSpinnerValues() {
        val values = mutableListOf<String>();
        val itemsList = walletsService.getAll();
        for(item in itemsList) {
            values.add(item.name);
        }
        spinnerWallet.adapter = ArrayAdapter(activity.applicationContext, R.layout.spinner_item, values);
    }

    private fun initServices() {
        walletsService = WalletsService(activity.applicationContext);
    }

    private fun findViews(view: View) {
        spinnerWallet = view.findViewById(R.id.spinnerWallet);
        buttonSort = view.findViewById(R.id.buttonSort);
    }
}
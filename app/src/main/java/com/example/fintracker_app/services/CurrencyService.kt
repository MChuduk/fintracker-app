package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.CurrencyModel
import com.example.fintracker_app.services.database.*
import kotlinx.coroutines.launch
import java.lang.Exception

class CurrencyService(val context: Context) {

    private val dbHelper = DatabaseHelper(context);

    suspend fun updateExchangeRates() {
        if (!UtilsService.isOnline(context)) {
            return;
        }
        try {
            val repo = Repository();
            val response = repo.getAllCurrency();
            val currencyList = response.body();

            for(currency in currencyList!!) {
                create(currency.id, currency.name, currency.exchange_rate);
            }
        } catch (error: Exception) {
            showMessage(error.message.toString());
        }
    }

    fun create(id: Int, name: String, exchangeRate: Float): CurrencyModel? {
        return try {
            val db = dbHelper.writableDatabase;
            val values = ContentValues();
            values.put(CURRENCY_ID, id);
            values.put(CURRENCY_NAME, name);
            values.put(CURRENCY_EXCHANGE_RATE, exchangeRate);
            val rowId = db.insertWithOnConflict(TABLE_CURRENCY, null, values,
                SQLiteDatabase.CONFLICT_REPLACE).toInt();
            CurrencyModel(id, name, exchangeRate);
        } catch (error: Exception) {
            showMessage(error.message.toString());
            null;
        }
    }

    private fun showMessage(message: String?){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
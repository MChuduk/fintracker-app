package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fintracker_app.data.repository.Repository
import com.example.fintracker_app.model.CurrencyModel
import com.example.fintracker_app.services.database.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

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
            showMessage(context, error.message.toString());
        }
    }

    fun create(id: Int, name: String, exchangeRate: Float): CurrencyModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            values.put(CURRENCY_ID, id);
            values.put(CURRENCY_NAME, name);
            values.put(CURRENCY_EXCHANGE_RATE, exchangeRate);
            val rowId = db.insertWithOnConflict(TABLE_CURRENCY, null, values,
                SQLiteDatabase.CONFLICT_REPLACE).toInt();
            CurrencyModel(id, name, exchangeRate);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }

    fun findByName(nameInput: String): CurrencyModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var currency: CurrencyModel? = null;
        try {
            val selection = "name = ?";
            val selectionArgs = arrayOf(nameInput);
            cursor = db.query(TABLE_CURRENCY, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, CURRENCY_ID);
                val name = cursor.getValueString(context, CURRENCY_NAME);
                val exchangeRate = cursor.getValueFloat(context, CURRENCY_EXCHANGE_RATE);
                currency = CurrencyModel(id, name, exchangeRate)
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return currency;
    }

    fun findById(idInput: Int): CurrencyModel? {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        var currency: CurrencyModel? = null;
        try {
            val selection = "id = ?";
            val selectionArgs = arrayOf(idInput.toString());
            cursor = db.query(TABLE_CURRENCY, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                val id = cursor.getValueInteger(context, CURRENCY_ID);
                val name = cursor.getValueString(context, CURRENCY_NAME);
                val exchangeRate = cursor.getValueFloat(context, CURRENCY_EXCHANGE_RATE);
                currency = CurrencyModel(id, name, exchangeRate)
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return currency;
    }

    fun getAll(): MutableList<CurrencyModel> {
        val db = dbHelper.readableDatabase;
        var cursor : Cursor? = null;
        val currencyList = mutableListOf<CurrencyModel>();
        try {
            cursor = db.query(TABLE_CURRENCY, null, null, null, null, null, null);

            if(cursor.moveToFirst()) {
                do {
                    val id = cursor.getValueInteger(context, CURRENCY_ID);
                    val name = cursor.getValueString(context, CURRENCY_NAME);
                    val exchangeRate = cursor.getValueFloat(context, CURRENCY_EXCHANGE_RATE);
                    currencyList.add(CurrencyModel(id, name, exchangeRate));
                } while (cursor.moveToNext());
            }
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
        } finally {
            cursor?.close();
            db.close();
        }
        return currencyList;
    }
}
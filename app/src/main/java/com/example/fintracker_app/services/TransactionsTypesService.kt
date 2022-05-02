package com.example.fintracker_app.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fintracker_app.model.TransactionCategoryModel
import com.example.fintracker_app.model.TransactionTypeModel
import com.example.fintracker_app.model.WalletModel
import com.example.fintracker_app.services.database.*

class TransactionsTypesService(val context: Context) {

    private val dbHelper = DatabaseHelper(context);

    init {
        create("Income");
        create("Spending");
    }

    fun create(name: String): TransactionTypeModel? {
        val db = dbHelper.writableDatabase;
        return try {
            val values = ContentValues();
            values.put(TRANSACTION_TYPE_NAME, name);
            val rowId = db.insertWithOnConflict(TABLE_TRANSACTION_TYPES, null, values,
                SQLiteDatabase.CONFLICT_REPLACE).toInt();
            TransactionTypeModel(rowId, name);
        } catch (error: Exception) {
            showMessage(context, error.message.toString());
            null;
        } finally {
            db.close();
        }
    }
}